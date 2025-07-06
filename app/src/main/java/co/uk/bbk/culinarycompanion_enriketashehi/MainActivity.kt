package co.uk.bbk.culinarycompanion_enriketashehi

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

/**
 * MainActivity is the single Activity hosting all the app's fragments.
 * It serves as the entry point of the application.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge layout so content can draw behind system bars (e.g. status bar).
        enableEdgeToEdge()

        // Sets the layout for this Activity to activity_main.xml,
        // which contains the NavHostFragment used for navigation.
        setContentView(R.layout.activity_main)
    }
}
