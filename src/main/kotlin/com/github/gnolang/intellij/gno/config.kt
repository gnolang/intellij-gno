package com.github.gnolang.intellij.gno

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.extensions.PluginId
import com.github.gnolang.intellij.gno.services.ConfigurationService
import java.nio.file.Paths
import java.io.File
import java.util.*

/**
 * ExtensionInfo is a collection of static information about the extension.
 * It gathers version, app information, and environment-specific details.
 */
class ExtensionInfo {
    private val version: String?
    val appName: String = ApplicationInfo.getInstance().fullApplicationName
    private val isPreview: Boolean
    private val isInCloudIDE: Boolean

    init {
        val pluginId = PluginId.getId(EXTENSION_ID)
        val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)
        version = pluginDescriptor?.version
        isPreview = false
        isInCloudIDE = System.getenv("CLOUD_SHELL") == "true"
                || System.getenv("CODESPACES") == "true"
                || System.getenv("GITPOD_WORKSPACE_ID") != null
    }
}

/**
 * Retrieves the 'gnopls' configuration for the given VirtualFile or project.
 */
fun getGnoplsConfig(project: Project, file: VirtualFile?): Properties {
    return getConfig("gnopls", project, file)
}

/**
 * Retrieves the 'gno' configuration for the given VirtualFile or project.
 */
fun getGnoConfig(project: Project, file: VirtualFile?): Properties {
    return getConfig("gno", project, file)
}

/**
 * Retrieves the configuration for a specific section.
 * If a file is not provided, it defaults to the project-level configuration.
 */
fun getConfig(section: String, project: Project, file: VirtualFile?): Properties {
    val properties = Properties()
    val configService = project.getService(ConfigurationService::class.java)
    configService.loadConfiguration(section, file, properties)
    return properties
}

/**
 * Retrieve the path to the 'gnopls' binary located in the extension's root directory.
 */
fun getGnoplsBinaryPath(): String? {
    val pluginId = PluginId.getId("com.github.gnolang.intellij.gno")
    val pluginDescriptor = PluginManagerCore.getPlugin(pluginId)
    val extensionRoot = pluginDescriptor?.pluginPath?.toAbsolutePath()?.toString() ?: return null
    var gnoplsBinaryPath = Paths.get(extensionRoot, "gnopls", "build", "gnopls").toString()
    gnoplsBinaryPath = correctBinname(gnoplsBinaryPath)

    if (File(gnoplsBinaryPath).exists()) {
        return gnoplsBinaryPath
    } else {
        println("gnopls not found at $gnoplsBinaryPath")
        return null
    }
}

/**
 * Corrects the binary file name based on the platform (adds '.exe' for Windows).
 */
fun correctBinname(binPath: String): String {
    return if (SystemInfo.isWindows) "$binPath.exe" else binPath
}

/** Static instance of ExtensionInfo that holds extension-related data */
val extensionInfo = ExtensionInfo()
