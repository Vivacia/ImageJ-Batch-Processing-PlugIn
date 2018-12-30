package com.mycompany.imagej;

import ij.IJ;
import ij.ImageJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import java.io.File;
import java.util.ArrayList;

/**
 * To load any image then image processing
 * (such as changing the color map, brightness, or contrast)
 * then save the file in another name
 *
 * @author Meghavarnika Budati
 */

public class Batch_PlugIn implements PlugInFilter {
	protected ImagePlus image;

	// image property members
	public static double width;
	public static double height;
	public static double x;
	public static double y;


	// plugin parameters
	public static String function;
	public static String cropOption;

	public void showAbout() {
		IJ.showMessage("Batch_PlugIn",
				"Batch processing of images"
		);
	}

	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("about")) {
			showAbout();
			return DONE;
		}

		image = imp;
		return DOES_8G | DOES_16 | DOES_32 | DOES_RGB;
	}

	@Override
	public void run(ImageProcessor imageProcessor) {

	}

	private static void showDialog() {
		GenericDialog gd = new GenericDialog("Process pixels");

		String[] items = new String[]{"Greyscale 8-bit", "Crop", "log 10"};

		// default value is 0.00, 2 digits right of the decimal point
		gd.addChoice("Function", items, "Select Option");

		gd.showDialog();
		if (gd.wasCanceled()) {
			return;
		}
		// get entered values
		function = gd.getNextChoice();

	}

	private static void cropDialog() {
		GenericDialog gd = new GenericDialog("Crop Image");

		gd.addNumericField("x value", x, 0);
		gd.addNumericField("y value", y, 0);
		gd.addNumericField("width", width, 0);
		gd.addNumericField("height", height, 0);

		gd.showDialog();

		x = gd.getNextNumber();
		y = gd.getNextNumber();
		width = gd.getNextNumber();
		height = gd.getNextNumber();

	}

	private static void cropDialog2() {
		GenericDialog gd = new GenericDialog("Method of Cropping");
		String[] methods = new String[]{"Automated Cropping", "Input the dimensions to crop"};
		gd.addChoice("Method", methods, "Select Option");

		gd.showDialog();

		cropOption = gd.getNextChoice();
	}

	public static void main(String[] args) throws Exception {
		Class<?> clazz = Batch_PlugIn.class;
		String url = clazz.getResource("/" + clazz.getName().replace('.', '/') + ".class").toString();
		String pluginsDir = url.substring("file:".length(), url.length() - clazz.getName().length() - ".class".length());
		System.setProperty("plugins.dir", pluginsDir);

		ArrayList<String> images = new ArrayList<>();

		String pathname = "C:\\Users\\varni\\Pictures\\test\\";
		File[] files = new File(pathname).listFiles();

		assert files != null;
		for (File file : files) {
			if (file.isFile()) {
				if (file.getName().contains(".tif")) {
					images.add(pathname + file.getName());
				}
			}
		}

		System.out.println(images);

		// start ImageJ
		new ImageJ();
		showDialog();

		if (function.equals("Crop")) {
			cropDialog2();
			if (cropOption.equals("Input the dimensions to crop")) {
				cropDialog();
			}
		}

		for (String imageName : images) {
			ImagePlus image = IJ.openImage(imageName);
			image.show();
			int number = Integer.parseInt(imageName.substring(imageName.indexOf(".") - 1, imageName.indexOf(".")));
			number++;
			String addressToSaveAt = imageName.substring(0, imageName.indexOf(".") - 1) + Integer.toString(number);
			if (function.equals("Greyscale 8-bit")) {
				IJ.run(image, "8-bit", "");
				IJ.saveAsTiff(image, addressToSaveAt);
				image.close();
			} else if (function.equals("Crop")) {
				if (cropOption.equals("Automated Cropping")) {
					String command = "python " + pathname + "crop_script_varnika.py" + " ";
					Runtime.getRuntime().exec(command + imageName);
					image.close();
				} else {
					IJ.makeRectangle((int) x, (int) y, (int) width, (int) height);
					IJ.run("Crop");
					IJ.saveAsTiff(image, addressToSaveAt);
					image.close();
				}
			} else if (function.equals("log 10")) {
				IJ.run(image, "Log", "");
				IJ.saveAsTiff(image, addressToSaveAt);
				image.close();
			} else {
				System.out.print("ERROR.");
			}
		}
		// run the plugin
		IJ.runPlugIn(clazz.getName(), "");
	}
}
