<h1>Применяемые разделы Java</h1>
<ul>
  <li>Collections</li>
  <li>I/O</li>
  <li>Exeptions</li>
  <li>Time</li>
  <li>Lambda Expressions</li>
  <li>Swing</li>
  <li>Annotations</li>
  <li>Maven</li>
</ul>

<h1>ПО при разработке</h1>
<ul>
  <li>ОС Windows 10 22h2</li>
  <li>IDE - IntelliJ Idea CE 2025</li>
  <li>JDK 2025 <a href="https://download.oracle.com/java/25/latest/jdk-25_windows-x64_bin.msi">скачать</a></li>
</ul>

<h1>Быстрый просмотр возможностей приложений</h1>
<p>В репозитории представлен код нескольких приложений. Для быстрого просмотра возможностей, предоставляемых этими приложениями, добавлен каталог <a href="jars">jars</a>.</p>

<p>В него помещены исполняемые jar-файлы приложений, которые можно запустить имея на ПК установленное ПО Java (JRE/JDK). Если приложение не запускается, возможна неполная установка Java (например отсутвие в перменной среды Path значения с папкой bin от Java) или устаревшая версия установленного JRE/JDK.</p>

<h1>Описание приложений</h1>
<h2>"ФотоПросмотр"</h2>
<ul>
  <li>код - <a href="ch11/src/main/java/testing/PhotoViewer">ch11/src/main/java/testing/PhotoViewer</a></li>
  <li>jar - <a href="jars/PhotoViewer.jar">jars/PhotoViewer.jar</a></li>
</ul>
<p></p>

<p>Представляет собой средство просмотра изображений форматов png, jpg, jpeg, присутсвующий в выбранном каталоге. На главном экране изображено: 
<ul>
  <li>
    текущее изображение большим планом, пять соседних изображений в виде панели из пяти малых изображений
  </li>
  <li>
    строка полного имени файла с изображением
  </li>
  <li>
    кнопки переключения на следующее/предыдущее изображение
  </li>
  <li>
    строка меню, с помощью которой можно прекратить работу приложения, выбрать текущую папку, переключить текущее фото в отдельное окно и получить инфрмацию о текущем изображении
  </li>
  <li>
    всплывающее меню на области изображения, позволяющее переключить в отдельное окно и получить информацию о изображении
  </li>
</ul></p>
<p>Дейсвия экранных кнопок продублированы нажатиями клавиш клавиатуры и кнопок мыши: на главном экрае можно пользоваться мнемониками строки меню и правой/левой стрелкой. В отдельном окне нажимать +/-, ПКМ/ЛКМ для зума</p>
<p></p>
<p>Пример главного окна на выбранной лиректории</p>
<img src="photos/PhotoViewer/0.webp">
<p></p>
<p>Всплывающее меню с выбором перехода в отдельное окно</p>
<img src="photos/PhotoViewer/1.webp">
<p></p>
<p>Изображение в отдельном окне с коэффициентом увеличения 0.4</p>
<img src="photos/PhotoViewer/2.webp">

<h2>Калькулятор</h2>
<ul>
  <li>код - <a href="ch11/src/main/java/testing/Calculator.java">ch11/src/main/java/testing/Calculator.java</a></li>
  <li>jar - <a href="jars/Calculator.jar">jars/Calculator.jar</a></li>
</ul>
<p>Представляет собой калькулятор, позволяющий:</p> 
<ul>
  <li>
    Вычилсять значения арифметических выражений содержащих операции: сложения, вычитания, целогисленного деления и умножения над целыми числами.
  </li>
  <li>
    Выбирать систему счисления, в которую приводится результат: 2, 8, 10 и 16.
  </li>
</ul> 
<p>Выражения могут содержать знаки операций и цифры. Длина выражения ограничена сорока символами. Результат появляется при нажатии кнопки "=". На экране присутсвует панель с 16-ю кнопками, "дисплеем" в верхней строке выражени, в нижней - результат.</p>
<p>Управление приложением, в том числе выбор системы счисления осуществляется через строку меню в верху окна</p>
<p>Пример вычисления</p>
<img src="https://github.com/user-attachments/assets/17e411ba-c162-4e2c-a64c-75934b23378f">
<p></p>
<p>Выбор двоичной системы счисления</p>
<img src="https://github.com/user-attachments/assets/55ce830d-c4ae-4826-a96a-159bb8d4be75">
<p></p>
<p>Пример расчета с учетом двоичной системы счиления</p>
<img src="https://github.com/user-attachments/assets/1c641c8b-5509-44ce-b4cc-ed34bcc72f2b">

**Пятнашка 3*3**

![4](https://github.com/user-attachments/assets/68cdc370-9539-499f-b533-c9a0f8bb10f8)
![3](https://github.com/user-attachments/assets/f9f861a8-2d90-4a8e-982c-0b496a2b380b)
![2](https://github.com/user-attachments/assets/e7334548-6b6f-43b9-84c9-61a3a65937a2)
![1](https://github.com/user-attachments/assets/c1b30296-4723-4729-ad1d-a2d4419176ee)
![0](https://github.com/user-attachments/assets/a071a054-4afe-4fd0-8858-4177f18f607f)
![5](https://github.com/user-attachments/assets/f6ef9cf4-f6fe-4c3d-96db-782387a0dd80)

**Дизайн строки**

![0](https://github.com/user-attachments/assets/42b5190c-56aa-47fa-a174-b341ad467603)
![3](https://github.com/user-attachments/assets/a8290f01-9109-4487-98be-6ad8bad3ef2f)
![2](https://github.com/user-attachments/assets/fa3abc8f-8e78-4372-a7bd-b359de27c2c1)
![7](https://github.com/user-attachments/assets/507328b7-2f24-4681-911f-7a46c182a72c)
![5](https://github.com/user-attachments/assets/fe7f2d2e-e73c-4c80-b3bb-f9b5d8ebf302)
![6](https://github.com/user-attachments/assets/caee7ed9-123f-45c5-aba5-8855d428eb5d)
![4](https://github.com/user-attachments/assets/19e79265-7df5-4c0a-836f-73e0ebd80b47)
![1](https://github.com/user-attachments/assets/ee58fc6d-e9ee-4e88-8416-cf0cdc6c96db)

