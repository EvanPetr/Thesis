input metauml;
beginfig(1);
Class.Audio("Audio")("+stream: AudioStream", "channel: Channel")("+WriteToFile: File");              % 1. Define the objects
Class.B("B")()();
B.w = Audio.e + (20, 0);           % 2. Position the objects
Class.C("C")()();              % 1. Define the objects
Class.D("D")()();
D.w = (100, 0);
C.w = (150, 0);           % 2. Position the objects
drawObjects(Audio, B, C, D);             % 3. Draw the objects
link(inheritance)(D.e -- C.w); % 4. Draw links between objects
link(inheritance)(B.w -- Audio.e); % 4. Draw links between objects
endfig;
end