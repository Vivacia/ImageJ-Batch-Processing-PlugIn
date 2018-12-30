import argparse
import numpy as np
from PIL import Image

BORDER_THRESH = 150

parser = argparse.ArgumentParser(description="Crop script v0.1")
parser.add_argument("input_filename", metavar="filename", type=str,
                    help="file to crop with this tool")
parser.add_argument("-t", "--threshold", metavar="T", type=int, nargs=1,
                    help="manually set edge threshold")
parser.add_argument("-v", "--verbose", action="store_true",
                    help="whether to use verbose output")

args = parser.parse_args()

if args.threshold:
    BORDER_THRESH = args.threshold

if args.verbose:
    print("Opening image '%s'" % args.input_filename)

im = np.array(Image.open(args.input_filename))

bounds = []

for i in range(0,im.shape[0]//2):
    if im[i,:,:3].mean() < BORDER_THRESH:
        bounds.append(i)
        break

if len(bounds) < 1:
    raise Exception("Could not detect top bound!")

if args.verbose:
    print("Detected top bound as %d pixels" % bounds[0])

for i in range(im.shape[0]-1,im.shape[0]//2,-1):
    if im[i,:,:3].mean() < BORDER_THRESH:
        bounds.append(i)
        break

if len(bounds) < 2:
    raise Exception("Could not detect bottom bound!")

if args.verbose:
    print("Detected bottom bound as %d pixels" % (im.shape[0] - bounds[1] - 1))

for i in range(0,im.shape[1]//2):
    if im[:,i,:3].mean() < BORDER_THRESH:
        bounds.append(i)
        break

if len(bounds) < 3:
    raise Exception("Could not detect left bound!")
    
if args.verbose:
    print("Detected left bound as %d pixels" % bounds[2])

for i in range(im.shape[1]-1,im.shape[1]//2,-1):
    if im[:,i,:3].mean() < BORDER_THRESH:
        bounds.append(i)
        break

if len(bounds) < 4:
    raise Exception("Could not detect right bound!")

if args.verbose:
    print("Detected right bound as %d pixels" % (im.shape[1] - bounds[3] - 1))

number_as_string = args.input_filename[args.input_filename.rfind(".") - 1 : args.input_filename.rfind(".")]
number = int(number_as_string)
number = number + 1
output_filename = args.input_filename[:args.input_filename.rfind(".") - 1] + str(number) + ".tif"

if args.verbose:
    print("Saving cropped image as '%s'" % output_filename)
    
Image.fromarray(im[bounds[0]:bounds[1]+1,bounds[2]:bounds[3]+1]).save(output_filename)
