# import tkinter as tk;
# import os;
# 
# from subprocess import check_output;
# from threading import Thread;
# 
# master = tk.Tk();
# 
# master.title("Minecraft Debug Interface");
# master.configure(background = "darkgray");
# 
# master.resizable(False, False);
# master.geometry("800x600");
# 
# console_debug = tk.Text(master, background = "gray", fg = "white", font = "white");
# console_debug.place(x = 0, y = 300, width = 800, height = 300);
# 
# run_debug = False;
# debug_cache = "Minecraft Debug Initialized";
# 
# console_debug.insert(1.0, debug_cache);
# 
# master.mainloop();

import os;

os.system("gradlew RunClient");