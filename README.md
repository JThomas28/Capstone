# Capstone
RingReader
This is a capstone project, coded in Java, that uses image analysis to detect and count tree rings from an image.

Each class has its own documentation, but here is a brief rundown of how the system works.

There are 2 packages in this project, one called UIStuff and the other called analysisStuff.
UIStuff contains all the UI stuff, while the other package contains all analysis.

UIPackage:
The GUI class is responsible for all the UI. It displays all the panels, images, dialog prompts, and results.
It also calls on helper classes, such as openWebPage, FileChooser, and Constants to use their methods and values

The Constants class contains nearly all strings needed by the UI. This is to make changing the code a snap, since all values are in the same file.

AnalysisStuff:
The analysis is done using 3 classes: Detector, Ring, and TreeObj.

TreeObj is a tree object. It contains fields such as age and image, where image is the image uploaded by the user that will be associated with the specified tree object.

Ring is a ring object. It contains fields such as width and starting/ending points. A new ring is created each time a darker pixel is detected by detector.

Detector is responsible for the detection of darker pixels. It has a threshold, starting point, and an array representing which direction(s) to measure in. Detector works outward, from the starting point, in each direction until it hits the edge of the image. It evaluates the color values of all pixels it encounters along the way. The pixel color values are all normalized by dividing by 100,000 to reduce the range of their color values. This allows for more accurate color analysis.
