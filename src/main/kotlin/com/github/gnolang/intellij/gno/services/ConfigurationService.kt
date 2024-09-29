package com.github.gnolang.intellij.gno.services

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.util.Properties

@Service
class ConfigurationService(private val project: Project) {

    /**
     * Loads the configuration for a given section (like 'gnopls' or 'gno').
     * This configuration can be project-wide or file-specific.
     * @param section The section of the configuration to load.
     * @param file The virtual file for which the configuration applies, or null for the project level.
     * @param properties The Properties object to populate with the configuration.
     */
    fun loadConfiguration(section: String, file: VirtualFile?, properties: Properties) {
        when (section) {
            "gnopls" -> loadGnoplsConfig(file, properties)
            "gno" -> loadGnoConfig(file, properties)
            else -> {
                // Default or unknown section
                properties.setProperty("unknownSection", "true")
            }
        }
    }


    /**
     * Loads the 'gnopls' configuration. Adjust the path and properties based on project or file.
     * @param file The file for which to load the configuration, or null for project level.
     * @param properties The Properties object to populate.
     */
    private fun loadGnoplsConfig(file: VirtualFile?, properties: Properties) {
        // Load 'gnopls' settings (example values)
        properties.setProperty("gnopls.enabled", "true")
        properties.setProperty("gnopls.path", findGnoplsBinaryPath())

        // Additional configuration logic for the file or project level can go here
        if (file != null) {
            properties.setProperty("gnopls.fileSpecificConfig", "valueForFile")
        } else {
            properties.setProperty("gnopls.projectConfig", "valueForProject")
        }
    }

    /**
     * Loads the 'gno' configuration for the given file or project.
     * @param file The file for which to load the configuration, or null for project level.
     * @param properties The Properties object to populate.
     */
    private fun loadGnoConfig(file: VirtualFile?, properties: Properties) {
        // Example configuration for 'gno'
        properties.setProperty("gno.languageServer", "enabled")

        // File-specific configuration if applicable
        if (file != null) {
            properties.setProperty("gno.fileSpecificSetting", "fileValue")
        } else {
            properties.setProperty("gno.projectSetting", "projectValue")
        }
    }

    /**
     * Helper function to find the path to the 'gnopls' binary.
     * @return The path to the 'gnopls' binary as a string, or a default value if not found.
     */
    private fun findGnoplsBinaryPath(): String {
        // Logic to find the path to the 'gnopls' binary, return a default path for now
        return "/usr/local/bin/gnopls"
    }
}
