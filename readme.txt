权威实验要求，钦定的，一定要兹磁：
	本次实验比较清晰，就不专门出指导书了，就简单规定三个问题：1）能够追踪出租车从程序启动运行以来的乘客服务情况及服务过程中行驶的路段。要求按照乘客服务进行区分管理。乘客服务阶段指的是从4个状态中的正在服务状态。2）迭代层数为一层，即测试者输入服务的第几次，程序返回相应的迭代器即可3）由于车的运动较快，可能在迭代器产生大量的数据，所以出租车的服务不要过于多了。
上次Bug：
	没有被发现

Readme
1.	主程序是DispatchingSystem.java，所有的命令都在这里输入，不要运行（run）其他程序。（DijkstraTest.java，test.java是我自己写来测试某些方法的，不是测试线程，跟本次作业没有关系，莫要运行）
2.	接上一条，MapInfoPri.java中的类已废，精神上已经被排除出了本次程序。
3.	信用度优先，相同则距离优先，距离相同则考虑流量，再相同则随机选择。
4.	抢单的时候，车辆位置是当时的位置；发车的时候，由于抢单到发车，车又闲逛了一会，所以取发车时的位置计算路径而非抢单时的位置。
5.	请不要让同一辆车一次抢多单。基本法中是“可以”抢多单，所以这里特别规定，以此为准。
6.	如果处于等待服务状态，车子在自己漫无目地乱走碰运气。
7.	输入请求的格式是(位置的行,位置的列).(目标的行,目标的列)。这样的请求是一个请求。禁止在一个输入请求得到输出的所有结果之前再次输入请求，否则造成的错误本人不予承认。这里的行列是0-79的数字。如果输入的请求时原地到原地，这样的请求会被认为是无效的请求。
8.	“出租车有信用积累，初始所有车信用为 0，每抢单一次会使其信用度加 1， 每成功服务顾客一次会使其信用度加 3”这里的理解，我认为应该是指：抢单就立即加一，被最终选定为服务顾客的车辆就可以使得信用度在原先加了一的基础上再加3，所以“成功服务顾客一次····”被理解为信用度总共加了4。
9.	地图的格式要求：按照基本法中的要求；行与行之间不得有空行；每行的数字之间由逗号“,”隔开。请测试者保证地图连通性和请求命令的合法性和格式的符合要求，否则造成的错误本人不予承认。地图严格按照基本法输入，文件名为mapPrint.txt,位置在该代码所在工程的目录下，而非代码目录下。
10.	交叉信息文件附上，和地图在同一目录下。
11.	如果一个请求没有被安排出租车服务，会返回无车可用，这个请求就被忽略了。今后都不会再处理。
12.	一次性总共不得超过400个未处理请求。
13.	如果有一个请求，对于两个车来说同样方便（信用度，距离等等都一样），由于程序处理的问题，可能导致车号小的车先抢到号。
14.	如果一个请求是超出地图之外的、目的地就是原地的，这样的请求会被忽略。
15.	禁止将一连串的请求复制粘贴进去，否则测试者认为造成了的问题本人一概不承认。
16.	司机都是敬业爱岗的，只要他们对某个请求抢了单，那么，他们抢单后无论处于什么状态，只要最终被决定为响应某个请求的车，这个司机就会启动，哪怕他当时正处于无目的行驶20秒后的懈怠期，他也会立刻来劲。
17.	读取某个出租车状态的话不能输入【1，80】之外的出租车号，否则出现的一切问题本人不承认。
18.	如果你觉得程序输出的哪辆车最终响应了那个请求，这段时间超过了3秒，（这是由于动不动就要求最短路径，耗时间），并不意味着这里的暂停3秒就是没有实现的。因为每一个请求有窗口时间，那是请求的生命周期，并不意味着生命周期结束了就要被即刻处理，因为在现实中，我们统计在窗口时间中出现在附近的所有的出租车收集起来，判断他们是否能够相应这个请求，窗口时间只是表示其后出现的请求我们不予考虑而已，而考虑这种行为本身是需要时间的。这个道理好比是评成绩的时候只考虑deadline前上交的作业，但不意味着deadline结束的时候你的成绩就会立即出来。所以这里就算是有时间误差也不能说明我的程序有问题。
19.	如果测试者想通过自己计时来判断本程序在时间处理方面的问题，是没有很大说服力的。因为我们没有办法掌握系统后台的时间安排，以我们人类的精度去判断计算机的精度，并且还要以此评判程序根据计算机运算原则做出的时间处理，无疑是天方夜谭，这样的想法也纯属吹毛求疵。
20.	第八次作业说的流量，这里的理解是这样的：从A到B点的流量不同于从B到A的流量。在现实中就说明道路是中间隔开的，一边只能单向行驶。如有与基本法冲突，请以这里为准。
21.	流量每100ms计算一次，单位为（辆每100毫秒）。
22.	这里作一个规定。调度系统调度车辆的时候会对比车辆的信用度，距乘客的距离等要素，但是不会比对车流量。车流量只会对司机在实际驾驶的时候（即将服务，正在服务状态时）的具体路径选择上产生影响。
23.	请务必在测试本程序之前调整虚拟机的内存配置信息，扩大虚拟机内存以适应程序对内存的需求。测试者请根据自身的条件搜索相应的方法。例如Eclipse环境可参考http://www.cnblogs.com/zhenmingliu/p/4153296.html。如果因为忽视了本条而产生的任何错误，包括是红字crash，本人都不予承认。如果内存调节地不够，请调到够大了再测（本人的电脑调到768m可正常运行），否则由此产生的一切错误本人不予承认。
24.	对地图的修改，请测试者严格按照基本法规定1的4）条目规定的进行。如果有违其中任意一个要求造成的错误本人不予承认。
25.	对基本法条目1的4）进行进一步的说明。地图点的改动不得超过5次，是总共。如果已改动了5次，想改别的，请先将地图恢复原状后再修改。恢复原状请用恢复初始地图指令。详见使用说明。地图的连通性由测试者保证。如果改地图后地图不联通，导致的错误本人不予承认。
26.	基本法中反复出现的“如果···相同，则随机选择····”在本程序中，这一原则在某些地方被简化为“如果···相同，则选择第一个被遍历到的···”。随机选择的真谛就是随便选一个，那么其他条件都一样的n种情况，我选择第一种，自然不违反随机的原则，况且这个情况之所以排到第一个也是一种随机。基本法在这里的随机其实实际上就是一种“不做要求”，本来就是可以自定义的。
27.	输出结果可能出的很慢，请耐心等待。
28.	规格在代码中，被注释起来的方法不属于该程序的功能部件，可以无视之。跪求勿在此处做大作文章批判我一番。
29.	（第九次作业从此开始，之前的readme和使用方法皆有效，除非在这之后特别说明）车辆如果按照既定路线行走遭遇红灯，就会被红灯阻塞而非另找他路。到了绿灯时，车辆会按照原先预计方向走。
30.	由于功能的增添，我的代码的实现逻辑导致程序运行所需时间极大地增长了。所以想要以0.1秒通过格子边，是不可能的。不过还好指导书在这方面的规定不是强制要求。所以在这里我就规定了哈···不要在运行时间上做文章，这里车辆的分派和运行时间以及您所理解的抢单窗口时间都比指导书上要求的长，具体长了多少秒，就以实际为准吧。给个大概数字，每走过一个点就会耗时5-7秒，请自行估计大概输出的时间，并保证不要在结果输出完成之前输入下一个请求。
31.	（第十次作业从此开始，前面的readme中的条目如果不明确说明作废的的话，依然有效）
32.	1-70号车是普通车，71-100是特权车。
33.	调度系统对请求附近的车辆的安排是按照关闭了的地图来进行的。如果地图中某一条路关闭，那么某个请求获取车辆的时候，可能会考虑请求地点到车的距离，这个距离是按照关闭了的地图来算的，就算是特权车也按照这个来计算。
34.	接上一条，特权车被安排了请求之后实际运行时才会用按照无视道路关闭的原则运行。
35.	程序的三点简短要求中有一则，说请求次数不能太多。所以请不要输入太多的请求，个位数就好。
36.	迭代层数为1层，根据那三点要求，这里更进一步规定：这里迭代器返回的内容只包含某一次服务的路线记录，不包含总共服务的次数。尽管吴际在课件上说了要“能够访问服务了多少次”，但是那三点具体要求中说了只要返回某一次特定的服务的记录就可以了，这么一来，返回总共服务了多少次没有意义。如果你非要知道服务了几次，可按照使用说明中的要求来添加输出项，但这就不关迭代器什么事了。所以这里可能存有的矛盾在这里说明，以这里的解释为准。
37.	关于前一次和这一次的功能，在前一次的基本法中和这一次的三个要求中若没有明文规定是错误的东西都不能算错。如判这样的错误，本人一概不予承认。
38.	类设计的一些文档在代码中。请进代码查看。
39.	吴际的课件里有关测试的东西，初始化出租车对象和位置之类的，请测试者自行设计。如果不想设计，可以就原样运行。原本的程序设计的就是所有出租车的出生点均随机。（“增加初始化出租车对象和位置“这句话不觉得有点不知所云吗？）
40.	NewTest.java是测试类，与出租车运行相关的功能没有什么关联，所以不写任何文档。
41.	如果找到了错误，多谢警醒。
42.	接上一条，如果真的发现了一个我的错误，那么由此产生的相关错误，比如跟别的情况排列组合产生新的错误，我将此认定为重复扣分。构建和谐六系，请不要这么做。
使用说明：
1.	请求输入有四种，分别是乘客请求、查看所有处于等待服务的出租车部分状态的请求、查看某个出租车状态的请求，修改某个点的请求，恢复地图的请求。乘客请求如之前readme记录第六条所说。所有等待服务的出租车的状态查询的格式是：”getAllFreeTaxis”；第三种请求是"seeStatusOfTaxi[0-9]+"(不包括引号)。读取某个出租车状态的话不能输入【1，80】之外的出租车号，否则出现的一切问题本人不认可。
2.	输入的请求必须一条一条输入，输入一个请求，按一次回车，否则造成的错误本人概不承认。
3.	代码Taxi1.java的readInfo()方法可以扩展，这个方法是用来获取本出租车的某些状态的，因为其功能可实现的弹性很大，所以可以作为“接口“使用。
4.	Taxi.Java可使用的属性如下（请不要用其他属性。这个类中的属性···，跪求不要在这里做文章）：（大小写和源代码中有出入，系word所致）
a.	Int Name，车辆编号
b.	Int srvTime, 服务次数
c.	String Plate，车牌
d.	Int Status车辆状态，1正在服务，2即将服务，3等待服务4.停止
e.	Position txLoc: Position类型，表示当前位置
f.	Int Credit:信用点数
g.	Time：发车时间（初始化时定义，后来不变，要求时间就即时获取系统时间减去这个time就可以得到我们所求的相对时间。）
5.	地图的修改可以使用命令changeMap\\([0-7]{0,1}[0-9],[0-7]{0,1}[0-9]\\)[0-3]{0,1}来修改某一个点的状态，例如changeMap(12,15)3将(12,15)（注意输入法的中英文，请使用 英文输入法输入，并保持与例子相同）(如果输入的是例如changeMap(12,15)而没有最后一个数字，默认是将这一点的信息改成0，而不是报错)用restoreDefaultMap命令恢复最初始的地图。修改地图上点不能超过5 次（比如把一个点原先是2，改成0，又将这个点改为1，再 将这个点改回2，就改了3次了），如果已经改了5 次，再改点会看到错误提示信息。如果要将某个点的状态改为某个状态(状态指基本法要求的0123四个数字)，而那个点的状态已经是原来的样子了，也会收到相应提示。
6.	如果要生成迭代器，命令是”iterateTaxi[0-9]{1,3}service[0-9]+”，不要输入[71,100]之外的车号，不要输入小于1的服务的序号。也不要输入没有服务过的车辆的序号。可以通过使用之前几条说的命令查看服务次数后再用这个指令。
7.	迭代器产生后会储存起来，可以在DispatchingSystem.java 的365行左右处自行增添代码。不要做和程序功能无关的修改。
8.	跟迭代器这里只提供了迭代器的增添的操作。如果想增加操作，可以在NewTest.java中添加修改方法。建议用静态方法。Request类中可能有助于您设计测试方法，如有需要可以看看。
9.	如果测试者的操作逾越了上述范畴，即做出了上面没有说过能做的操作，造成的错误本人概不承认。

设计文档
一些跟readme有点像的信息 
1.	根据吴际老师上课所传达的精神，这里不给run方法和main方法写具体的文档。
2.	文档中写的表示对象不包括所有的类中属性，有的属性比如一些flag和一些中间变量等，本就没有什么可说的，或者说不太重要，没什么意义，于是不予写明。
3.	这里猜测ppt中的<>是指不等于（要么大于要么小于），而我的文档中很多地方由于对表达清晰的追求，没有用这个符号，而是采用了经典的!=符号。还有的类的对比用到了.equals()来表述相等。
4.	所有的信息详见代码。
LSP分析
1.	首先在出租车初始化阶段，我声明的都是父类型，但是前70个用的是父类出租车的构造，后30个用的是子类，也就是特权出租车的构造方法。这样就可以用一个数组调度两种车了。
2.	itrList 是一个Iterator类的数组，通过对出租车类的强制转换，可以将出租车转换为Taxi2，从而可以用特权出租车的迭代器生成方法。

