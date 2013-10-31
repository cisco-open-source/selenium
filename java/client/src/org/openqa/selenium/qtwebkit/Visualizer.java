package org.openqa.selenium.qtwebkit;

public interface Visualizer {
    /**
     * Similar to {@link org.openqa.selenium.WebDriver#getPageSource()} but
     * result of this one is usable for independant visualization in borwser.
     * It means that:
     * <ul>
     * <li>Images are downloaded and placed using data URL scheme
     * <li>Stylesheets are assembled with HTML
     * </ul>
     */
    public String getVisualizerSource();
}