# JavaForEV3
Java library for manipulate ev3 brick

The EV3 brick supports multiple communication interfaces: Bluetooth, USB and WiFi. The EV3 protocol is the same for all 3 transport technologies.

References:
1. Bluetooth реализация ICommunicator <https://github.com/megavice1753/BluetoothCommunicator>
2. USB реализация ICommunicator <https://github.com/megavice1753/EV3USBConnect>
3. [Communication dev kit](https://www.lego.com/cdn/cs/set/assets/blt6879b00ae6951482/LEGO_MINDSTORMS_EV3_Communication_Developer_Kit.pdf)
4. [Firmware dev kit](https://www.lego.com/cdn/cs/set/assets/blt77bd61c3ac436ea3/LEGO_MINDSTORMS_EV3_Firmware_Developer_Kit.pdf)

Usage examples:
``` java
BluetoothCommunicator bt = new BluetoothCommunicator("0016534307e2");//Uniq bluetooth code
USBCommunicator usb = new USBCommunicator(); //инициаллизация транспортного протокола по usb

ICommunicator com = usb;

Brick br = new Brick(com, 0); //инициаллизация ev3, транспортный  протокол - usb

br.checkAllPorts(); //получить состояние всех входных и выходных портов

br.movePortN(PORTD, 10); // вращать сервомотор D со скоростью 10%

float fd = br.getWorkingPort(PORTD).readValue(-1); //прочитать кол-во вращений с сервомотора D

        
br.clearCountAllPorts(); //сбросить состояния всех портов

br.panelColor(Panel.GREEN_FLASHING); // мигать зеленым цветом световой панелью ev3

br.checkAllPorts();

br.movePortN(PORTB, 10);

br.stopAllPorts(); // остановить все сервомоторы

byte[] prts = br.getOutputPorts();  //получить список всех сервомоторов

br.movePorts(prts, 20); //пошевелить всеми доступными сервомоторами

br.stopPorts(prts); //остановить все доступные моторы

br.movePortN(PORTB, 10);

br.polarityPorts(prts,0);  //изменить направление вращения сервомотора

br.stopPortN(PORTB);

br.moveAllPorts(20); //установить скорость вращения всем сервомоторам в 20%
```
