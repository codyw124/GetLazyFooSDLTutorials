package getlazyfoosdltutorials;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

class GetLazyFooSDLTutorials {
	public static void main(String[] args) throws Exception {

		String[] tuts = { "01_hello_SDL", "02_getting_an_image_on_the_screen", "03_event_driven_programming",
				"04_key_presses", "05_optimized_surface_loading_and_soft_stretching",
				"06_extension_libraries_and_loading_other_image_formats", "07_texture_loading_and_rendering",
				"08_geometry_rendering", "09_the_viewport", "10_color_keying", "11_clip_rendering_and_sprite_sheets",
				"12_color_modulation", "13_alpha_blending", "14_animated_sprites_and_vsync", "15_rotation_and_flipping",
				"16_true_type_fonts", "17_mouse_events", "18_key_states", "19_gamepads_and_joysticks",
				"20_force_feedback", "21_sound_effects_and_music", "22_timing", "23_advanced_timers",
				"24_calculating_frame_rate", "25_capping_frame_rate", "26_motion", "27_collision_detection",
				"28_per-pixel_collision_detection", "29_circular_collision_detection", "30_scrolling",
				"31_scrolling_backgrounds", "32_text_input_and_clipboard_handling", "33_file_reading_and_writing",
				"34_audio_recording", "35_window_events", "36_multiple_windows", "37_multiple_displays",
				"38_particle_engines", "39_tiling", "40_texture_manipulation", "41_bitmap_fonts",
				"42_texture_streaming", "43_render_to_texture", "44_frame_independent_movement", "45_timer_callbacks",
				"46_multithreading", "47_semaphores", "48_atomic_operations", "49_mutexes_and_conditions",
				"50_SDL_and_opengl_2", "51_SDL_and_modern_opengl", "52_hello_mobile",
				"53_extensions_and_changing_orientation", "54_touches", "55_multitouch" };

		GetLazyFooSDLTutorials getLazyFooSDLTutorials = new GetLazyFooSDLTutorials();


		for (String x : tuts) {
			String generatedFile = getLazyFooSDLTutorials.saveTutorialTextToHtmlFile(x);
			getLazyFooSDLTutorials.turnHTMLFileToTXTFile(generatedFile);
		}
	}

	public void turnHTMLFileToTXTFile(String file) throws Exception {
		WebDriver driver = null;

		try {
			System.setProperty("webdriver.gecko.driver", "geckodriver");
			
			// open driver
			driver = new FirefoxDriver();

			File f = new File(file);

			// And now open file
			driver.get("file://" + f.getCanonicalPath());

			Actions action = new Actions(driver);
			// press ctrl a
			action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0061')).perform();

			// press ctrl c
			action.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0063')).perform();

			String grabbed = ReadClipboard();
			
			FileWriter fw = new FileWriter(f.getName().replace(".html", ".txt"));
			
			fw.write(grabbed);
			
			fw.close();
			
			f.delete();

		} finally {

			// Close the browser
			if (driver != null) {
				driver.quit();
			}
		}
	}

	public String saveTutorialTextToHtmlFile(String tutorial) throws Exception {
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		String newFileName = tutorial.replace('/', '-') + ".html";
		boolean withinHeader = false;
		FileWriter fw = new FileWriter(newFileName);

		if (tutorial.equals("06_extension_libraries_and_loading_other_image_formats") || tutorial.equals("01_hello_SDL")
				|| tutorial.equals("53_extensions_and_changing_orientation") || tutorial.equals("52_hello_mobile")) {
			url = new URL("https://lazyfoo.net/tutorials/SDL/" + tutorial + "/index2.php");
		} else {
			url = new URL("https://lazyfoo.net/tutorials/SDL/" + tutorial + "/index.php");
		}

		is = url.openStream(); // throws an IOException
		br = new BufferedReader(new InputStreamReader(is));

		while ((line = br.readLine()) != null) {

			// if this is the start of the header
			if (line.contains("<head>")) {
				// mark to stop reading from html
				withinHeader = true;

				// write my head'''
				fw.write(
						"<head><style>.tutCode, .nav, h1, .tutFooter, .footer, h6, .tutImg{display:none;}</style></head>");
			}

			if (line.contains("</head>")) {
				// mark to start reading from html again
				withinHeader = false;
			}

			// if its not a part of header
			if (!withinHeader) {
				// write to file
				fw.write(line);
			}
		}

		if (is != null)
			is.close();
		fw.close();

		return newFileName;
	}

	public String ReadClipboard() throws Exception {
		// get the system clipboard
		Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// get the contents on the clipboard in a
		// transferable object
		Transferable clipboardContents = systemClipboard.getContents(null);
		// check if clipboard is empty
		if (!clipboardContents.equals(null)) {

			// see if DataFlavor of
			// DataFlavor.stringFlavor is supported

			if (clipboardContents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				// return text content
				String returnText = (String) clipboardContents.getTransferData(DataFlavor.stringFlavor);
				return returnText;
			}
		}
		return null;
	}
}
