echo off
cls
csc.exe /t:exe /out:BillMaker.exe /win32icon:icon.ico .\ExeWrapper.cs
makensis.exe .\installer.nsi
pause