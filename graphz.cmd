@echo off
for %%f in (*.dot) do "C:\Program Files (x86)\Graphviz2.38\bin\dot.exe" -Tpng %%f -o %%f.png