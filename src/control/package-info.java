/**
 * Provides classes for managing the core logic and configurations of the Package Cost Calculator application.
 *
 * <p>The {@code control} package is responsible for handling the following functionalities:</p>
 * <ul>
 *   <li><b>Calculations:</b> Contains logic to calculate shipping costs based on package dimensions and weight.</li>
 *   <li><b>Configuration Management:</b> Manages loading, editing, and storing configuration entries, such as predefined
 *       package costs and other application settings.</li>
 *   <li><b>Data Validation:</b> Ensures input data adheres to the required constraints and formats.</li>
 * </ul>
 *
 * <p>Key Classes:</p>
 * <ul>
 *   <li>{@link control.Calculator} - Performs shipping cost calculations using package data.</li>
 *   <li>{@link control.ConfigHandler} - Handles the management and persistence of configuration entries.</li>
 *   <li>{@link control.ConfigEntry} - Represents individual configuration entries, such as package cost information.</li>
 * </ul>
 *
 * <p>This package serves as the application’s business logic layer, separating the GUI from data processing and configuration management.</p>
 *
 * @since 1.0
 * @author Benni
 */
package control;