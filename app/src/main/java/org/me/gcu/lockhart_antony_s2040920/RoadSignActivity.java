package org.me.gcu.lockhart_antony_s2040920;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * RoadSignActivity displays VMS (Variable Message Sign) messages
 * in an authentic digital road sign style with LED-like appearance and animation.
 */
public class RoadSignActivity extends AppCompatActivity {
    private TextView messageDisplay;
    private TextView locationDisplay;
    private MaterialButton prevButton;
    private MaterialButton nextButton;
    private MaterialButton playPauseButton;

    private DataManager dataManager;
    private List<VMSUnit> vmsUnits;
    private int currentMessageIndex = 0;
    private boolean isAutoPlaying = true;

    private Handler autoPlayHandler;
    private Runnable autoPlayRunnable;
    private static final int AUTO_PLAY_DELAY = 5000; // 5 seconds per message

    private ValueAnimator scrollAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_sign);

        initializeViews();
        loadVMSData();
        setupButtons();

        if (vmsUnits != null && !vmsUnits.isEmpty()) {
            displayMessage(0);
            if (isAutoPlaying) {
                startAutoPlay();
            }
        } else {
            messageDisplay.setText("NO MESSAGES AVAILABLE");
            locationDisplay.setText("Please load data first");
            disableNavigation();
        }
    }

    private void initializeViews() {
        messageDisplay = findViewById(R.id.messageDisplay);
        locationDisplay = findViewById(R.id.locationDisplay);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        playPauseButton = findViewById(R.id.playPauseButton);

        autoPlayHandler = new Handler(Looper.getMainLooper());
    }

    private void loadVMSData() {
        dataManager = new DataManager(this);
        vmsUnits = dataManager.getAllVMSUnits();
    }

    private void setupButtons() {
        prevButton.setOnClickListener(v -> {
            stopAutoPlay();
            showPreviousMessage();
        });

        nextButton.setOnClickListener(v -> {
            stopAutoPlay();
            showNextMessage();
        });

        playPauseButton.setOnClickListener(v -> toggleAutoPlay());

        updatePlayPauseButton();
    }

    private void displayMessage(int index) {
        if (vmsUnits == null || vmsUnits.isEmpty() || index < 0 || index >= vmsUnits.size()) {
            return;
        }

        currentMessageIndex = index;
        VMSUnit vmsUnit = vmsUnits.get(index);

        // Get the first message from the VMS unit
        String messageText = "NO MESSAGE";
        if (vmsUnit.getMessages() != null && !vmsUnit.getMessages().isEmpty()) {
            messageText = vmsUnit.getMessages().get(0).getTextContent();
        }

        // Display location/reference
        String location = "VMS: " + vmsUnit.getVmsUnitReference() +
                         " (" + (index + 1) + "/" + vmsUnits.size() + ")";
        locationDisplay.setText(location);

        // Animate the message display
        animateMessage(messageText);
    }

    private void animateMessage(String text) {
        // Cancel any existing animation
        if (scrollAnimator != null && scrollAnimator.isRunning()) {
            scrollAnimator.cancel();
        }

        messageDisplay.setText(text);

        // Create a subtle fade-in effect for message changes
        messageDisplay.setAlpha(0f);
        messageDisplay.animate()
                .alpha(1f)
                .setDuration(300)
                .start();

        // If message is long, add scrolling effect
        if (text.length() > 40) {
            messageDisplay.postDelayed(() -> enableScrolling(), 500);
        }
    }

    private void enableScrolling() {
        messageDisplay.setSelected(true);
        messageDisplay.setHorizontallyScrolling(true);
    }

    private void showPreviousMessage() {
        if (vmsUnits == null || vmsUnits.isEmpty()) return;

        int newIndex = currentMessageIndex - 1;
        if (newIndex < 0) {
            newIndex = vmsUnits.size() - 1; // Wrap to last message
        }
        displayMessage(newIndex);
    }

    private void showNextMessage() {
        if (vmsUnits == null || vmsUnits.isEmpty()) return;

        int newIndex = currentMessageIndex + 1;
        if (newIndex >= vmsUnits.size()) {
            newIndex = 0; // Wrap to first message
        }
        displayMessage(newIndex);
    }

    private void startAutoPlay() {
        isAutoPlaying = true;
        updatePlayPauseButton();

        autoPlayRunnable = new Runnable() {
            @Override
            public void run() {
                showNextMessage();
                autoPlayHandler.postDelayed(this, AUTO_PLAY_DELAY);
            }
        };

        autoPlayHandler.postDelayed(autoPlayRunnable, AUTO_PLAY_DELAY);
    }

    private void stopAutoPlay() {
        isAutoPlaying = false;
        updatePlayPauseButton();

        if (autoPlayHandler != null && autoPlayRunnable != null) {
            autoPlayHandler.removeCallbacks(autoPlayRunnable);
        }
    }

    private void toggleAutoPlay() {
        if (isAutoPlaying) {
            stopAutoPlay();
        } else {
            startAutoPlay();
        }
    }

    private void updatePlayPauseButton() {
        if (playPauseButton != null) {
            playPauseButton.setText(isAutoPlaying ? "⏸ PAUSE" : "▶ PLAY");
            playPauseButton.setIconResource(isAutoPlaying ?
                android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
        }
    }

    private void disableNavigation() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
        playPauseButton.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAutoPlay();
        if (scrollAnimator != null) {
            scrollAnimator.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (vmsUnits != null && !vmsUnits.isEmpty() && isAutoPlaying) {
            startAutoPlay();
        }
    }
}
