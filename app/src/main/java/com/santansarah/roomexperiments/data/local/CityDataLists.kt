import com.example.roomlists.data.local.City

/**
 * List of cities to prepopulate when the database is created.
 * City info provided by: https://simplemaps.com/data/us-zips
 */
val importList =
    listOf(
        City(0, "New York", "NY", 40.6943f, -73.9249f),
        City(0, "Los Angeles", "CA", 34.1141f, -118.4068f),
        City(0, "Phoenix", "AZ", 33.5722f, -112.0892f),
        City(0, "Houston", "TX", 29.7860f,-95.3885f),
        City(0, "Atlanta", "GA", 33.7628f,-84.4220f),
        City(0, "San Francisco", "CA", 37.7558f,-122.4449f))

/**
 * List of cities to add when we click the button. Our repo will
 * use this to simulate user input and populate the database.
 */
val cityList =
    listOf(
        City(0, "Seattle", "WA", 47.6211f, -122.3244f),
        City(0, "San Diego", "CA", 32.8313f, -117.1222f),
        City(0, "Denver", "CO", 39.7620f, -104.8758f),
        City(0, "Baltimore", "TX", 39.3051f,-76.6144f),
        City(0, "Las Vegas", "NV", 36.2333f,-115.2654f),
        City(0, "Austin", "TX", 30.3005f,-97.7522f))