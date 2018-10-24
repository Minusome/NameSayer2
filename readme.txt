  _   _                               _____
 | \ | |                             / ____|
 |  \| |   __ _   _ __ ___     ___  | (___     __ _   _   _    ___   _ __
 | . ` |  / _` | | '_ ` _ \   / _ \  \___ \   / _` | | | | |  / _ \ | '__|
 | |\  | | (_| | | | | | | | |  __/  ____) | | (_| | | |_| | |  __/ | |
 |_| \_|  \__,_| |_| |_| |_|  \___| |_____/   \__,_|  \__, |  \___| |_|
                                                       __/ |
                                                      |___/

What is it?
***********
Tool for learning unfamiliar names

License:
********
MIT License

Authors:
********
Tony Liu,
Lisa Wang


Requirements:
*************
● Oracle Java 8 version 1.8.0_121 or newer (Java 9 and 10 are not supported)
● Oracle JavaFX runtime (normally bundled with Java 8)
● FFmpeg version 3.4.4
● FFplay version 3.4.4
● Microphone and audio output
● Linux Operating System
● Intel HD 4000 graphics or better

(Warning: animations may drop frames if hardware acceleration is not supported)


Run Instructions:
*****************
Extract into working directory. You should see the following directory structure:

    pwd/
    | -- readme.txt
    | -- NameSayer.jar
    | -- database/
        |-- name1.wav
        |-- name2.wav etc.
    | -- generated/
        |-- attempts/
        |-- saved/
        |-- session/
            |-- assessment/
            |-- practise/
        |-- stats/
    | -- script/
        |-- VolumeEdit.sh/

Then run the following commands from the present working directory:

    user@shell:/pwd/$ chmod +x NameSayer.jar
    user@shell:/pwd/$ java -jar NameSayer.jar

FAQ:
****

> Main menu fails to load / Nothing displays / Assets Corrupted
    Solution : This is often caused by running the application with OpenJDK. Please check your java version by running:
               user@shell:/path-to-folder/$ java -version
               Switch to Oracle JDK if it is not already.

> Getting exception java.lang.ClassNotFoundException: com.sun.javafx.css.StyleConverterImpl
    Solution : This is caused when running the application with Java 9 or 10. Please downgrade your distribution to Java 8

> No audio during playback or after recording
    Solution : Install FFmpeg using the following link:
                https://github.com/adaptlearning/adapt_authoring/wiki/Installing-FFmpeg
                (Courtesy of Adapt)

> Can’t import new name folder
    Solution : Make sure only wav files are being imported

> Animations are choppy / Dropped frames
    Solution : Please ensure the application is not running within a VM and has hardware acceleration supported


Dependencies:
************
● JFoenix library - licensed under Apache License 2.0
● FontAwesomeFX - licensed under Apache License 2.0
● ControlsFX - licensed under BSD 3-clause license
● FFmpeg / FFplay - licensed under GNU Lesser General Public License version 2.1


Copyright (c) 2018 Tony Liu, Lisa Wang
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.