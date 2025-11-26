package de.capmo.capmomeasure

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform