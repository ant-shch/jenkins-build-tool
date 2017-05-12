@echo off
cd ..
FOR /D /r %%G in ("*.Tests") DO (
    FOR /D %%T in ("%%G\bin\*") DO (
	    For /F "tokens=*" %%F IN ('dir /b /s %%T\*.Tests.dll') DO (
		   nunit3-console.exe %%F
	    )))