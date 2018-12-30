<p>This is a program to run ImageJ with some added automated functions for batch processing of images. Operations that can be performed are:

<ol>
<li>Changing the brightness/contrast
<li>Performing log 10 on the images
<li>Turning images into 8-bit greyscale images
<li>Cropping: 
     <ol><li>auto-crop the box in images
     <li>cropping with user-supplied parameters</ol>
</ol>

<p>For cropping, run the given python 3 script.
<p>Requirements: install the modules <code>numpy</code> and <code>Image</code>.
<p>It can be done by:
<code>pip install module_name</code> or <code>sudo pip install module_name</code>

<p>Change line 109 to the path where the images required to be processed are stored.

<p>The python code must be in the same directory as the images for the program to run.
The given Java code should be cloned and run via an IDE.

<p>All images to be processed must end with a single-digit number. The processed image will be of the format:
Image2 if the supplied image name is Image1 and so on.