@echo off 
for /F "delims=" %%i in ('dir /ad/b/s') do (
    if %%~ni==build (
        echo ����ɾ������%%i
        rd /q/s %%i
    )
)
echo --------���������--------
pause>nul