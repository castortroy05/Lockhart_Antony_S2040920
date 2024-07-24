package org.me.gcu.lockhart_antony_s2040920;

import java.util.Objects;

public class Impact {
    private String delays;

    public Impact(String delays) {
        this.delays = delays;
    }

    // Getter and Setter
    public String getDelays() { return delays; }
    public void setDelays(String delays) { this.delays = delays; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Impact impact = (Impact) o;
        return Objects.equals(delays, impact.delays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(delays);
    }

    @Override
    public String toString() {
        return "Impact{" +
                "delays='" + delays + '\'' +
                '}';
    }
}