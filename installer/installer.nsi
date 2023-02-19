Name "BillMaker Installer"
OutFile "BillMakerSetup.exe"

RequestExecutionLevel user
InstallDir $LOCALAPPDATA\BillMaker

Page directory
Page instfiles

Section "Install"
	SetOutPath $INSTDIR
	File BillMaker.jar
	File BillMaker.exe

	WriteUninstaller $INSTDIR\uninstall.exe

	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BillMaker" \
                 "DisplayName" "BillMaker"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BillMaker" \
                 "UninstallString" "$\"$INSTDIR\uninstall.exe$\""

    CreateDirectory $SMPROGRAMS\BillMaker
	CreateShortCut $SMPROGRAMS\BillMaker\BillMaker.lnk $INSTDIR\BillMaker.exe
SectionEnd

Section "Uninstall"
	Delete $SMPROGRAMS\BillMaker\BillMaker.lnk
	RMDir $SMPROGRAMS\BillMaker

	Delete $INSTDIR\BillMaker.jar
	Delete $INSTDIR\BillMaker.exe
	Delete $INSTDIR\uninstall.exe
	Delete $INSTDIR

	Delete $LOCALAPPDATA\BillMaker\

	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BillMaker"

	SetAutoClose false
SectionEnd
