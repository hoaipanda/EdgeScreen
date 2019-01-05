call adb.exe devices
call adb.exe reboot bootloader
call fastboot.exe getvar all
call fastboot.exe oem unlock
call fastboot.exe format userdata
call fastboot.exe erase recovery
call fastboot.exe flash recovery recovery/recovery.by.gursewak.img
call fastboot.exe getvar all
call fastboot.exe reboot
echo done successful please press any key to continue
echo disconnect your phone and press power button untill phone reboots
pause