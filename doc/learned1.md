# Learned: Iterating over ArrayList vs ArrayDeque vs Array

## Summary

I found some interesting performance differences while solving the [timeseries-chalenge](https://github.com/cinovo/timeseries-challenge).
I assumed that iterating over an ArrayList should be comparable to iterating over an Array. But I found out that the opposite is true:
Iterating over an Array using
`````
for (int i = 0; i < array.length; i++) {} 
`````
is significantally faster than iterating over an ArrayList using
`````
for (Object o : arraylist) {}
`````

## Benchmark

`````
java version "1.7.0_07"
Java(TM) SE Runtime Environment (build 1.7.0_07-b10)
Java HotSpot(TM) 64-Bit Server VM (build 23.3-b01, mixed mode)
`````

* VM arguments: -Xmx2G -Xms2G -Xmn1G -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails
* call System.gc() to remove garbage from previous run
* iterating 1 mio times over the data structure to warm up the VM.
* call System.gc() to remove garbage from warmup
* begin = System.nanoTime()
* iteration 100000 times over the data structure
* end = System.nanoTime()
* runtime = end-begin

Result:

<table>
	<thead>
		<tr>
			<th>Implementation</th>
			<th>100 Elements</th>
			<th>1000 Elements</th>
			<th>10000 Elements</th>
			<th>100000 Elements</th>
			<th>1000000 Elements</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Learned1_ArrayListIterator</td>
			<td>30</td>
			<td>93</td>
			<td>936</td>
			<td>9622</td>
		</tr>
		<tr>
			<td>Learned1_ArrayListGet</td>
			<td>18</td>
			<td>80</td>
			<td>801</td>
			<td>9706</td>
		</tr>
		<tr>
			<td>Learned1_ArrayDequeIterator</td>
			<td>29</td>
			<td>206</td>
			<td>1959</td>
			<td>19485</td>
		</tr>
		<tr>
			<td>Learned1_IntegerArray</td>
			<td>5</td>
			<td>4</td>
			<td>5</td>
			<td>5</td>
		</tr>
		<tr>
			<td>Learned1_intArray</td>
			<td>5</td>
			<td>4</td>
			<td>5</td>
			<td>6</td>
		</tr>
	</tbody>
</table>

### Output: 100 Elements
`````
Learned1_ArrayListIterator
0.125: [GC [PSYoungGen: 15728K->400K(917504K)] 15728K->400K(1966080K), 0.0017110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.127: [Full GC (System) [PSYoungGen: 400K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 400K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0164580 secs] [Times: user=0.04 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
warmup done
0.312: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->394K(1966080K), 0.0008090 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.313: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 298K->294K(1048576K)] 394K->294K(1966080K) [PSPermGen: 2665K->2665K(21248K)], 0.0102250 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 30052000
run (ms): 30
Learned1_ArrayListGet
0.356: [GC [PSYoungGen: 31457K->128K(917504K)] 31752K->422K(1966080K), 0.0006580 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.356: [Full GC (System) [PSYoungGen: 128K->0K(917504K)] [ParOldGen: 294K->291K(1048576K)] 422K->291K(1966080K) [PSPermGen: 2668K->2668K(21248K)], 0.0068100 secs] [Times: user=0.02 sys=0.01, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.472: [GC [PSYoungGen: 15728K->32K(917504K)] 16020K->323K(1966080K), 0.0003060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.472: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 291K->291K(1048576K)] 323K->291K(1966080K) [PSPermGen: 2671K->2671K(21248K)], 0.0041630 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 18505000
run (ms): 18
Learned1_ArrayDequeIterator
0.496: [GC [PSYoungGen: 15728K->96K(917504K)] 16020K->387K(1966080K), 0.0004140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.497: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 291K->291K(1048576K)] 387K->291K(1966080K) [PSPermGen: 2675K->2675K(21248K)], 0.0046500 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.764: [GC [PSYoungGen: 15728K->32K(917504K)] 16020K->323K(1966080K), 0.0002840 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.765: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 291K->292K(1048576K)] 323K->292K(1966080K) [PSPermGen: 2683K->2683K(21248K)], 0.0062880 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 29019000
run (ms): 29
Learned1_IntegerArray
0.801: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->356K(1966080K), 0.0004660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.802: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 356K->293K(1966080K) [PSPermGen: 2687K->2687K(21248K)], 0.0042340 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
0.813: [GC [PSYoungGen: 15729K->32K(917504K)] 16022K->325K(1966080K), 0.0003930 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.814: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0053000 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 5180000
run (ms): 5
Learned1_intArray
0.826: [GC [PSYoungGen: 15728K->64K(917504K)] 16022K->357K(1966080K), 0.0005560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.827: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 357K->293K(1966080K) [PSPermGen: 2691K->2691K(21248K)], 0.0052430 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.839: [GC [PSYoungGen: 15729K->32K(917504K)] 16022K->325K(1966080K), 0.0002570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.839: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0041400 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 5755000
run (ms): 5
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x000000014c3a0000, 0x000000018c3a0000, 0x000000018c3a0000)
  eden space 786432K, 4% used [0x000000014c3a0000,0x000000014e258630,0x000000017c3a0000)
  from space 131072K, 0% used [0x00000001843a0000,0x00000001843a0000,0x000000018c3a0000)
  to   space 131072K, 0% used [0x000000017c3a0000,0x000000017c3a0000,0x00000001843a0000)
 ParOldGen       total 1048576K, used 293K [0x000000010c3a0000, 0x000000014c3a0000, 0x000000014c3a0000)
  object space 1048576K, 0% used [0x000000010c3a0000,0x000000010c3e96f0,0x000000014c3a0000)
 PSPermGen       total 21248K, used 2700K [0x00000001071a0000, 0x0000000108660000, 0x000000010c3a0000)
  object space 21248K, 12% used [0x00000001071a0000,0x00000001074432d0,0x0000000108660000)
`````

### Output: 1000 Elements
`````
Learned1_ArrayListIterator
0.126: [GC [PSYoungGen: 15728K->368K(917504K)] 15728K->368K(1966080K), 0.0016810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.127: [Full GC (System) [PSYoungGen: 368K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 368K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0122340 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
1.167: [GC [PSYoungGen: 47186K->128K(917504K)] 47484K->434K(1966080K), 0.0007120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.168: [Full GC (System) [PSYoungGen: 128K->0K(917504K)] [ParOldGen: 306K->312K(1048576K)] 434K->312K(1966080K) [PSPermGen: 2665K->2665K(21248K)], 0.0062400 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 93255000
run (ms): 93
Learned1_ArrayListGet
1.269: [GC [PSYoungGen: 31457K->64K(917504K)] 31769K->376K(1966080K), 0.0004910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.269: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 312K->291K(1048576K)] 376K->291K(1966080K) [PSPermGen: 2668K->2668K(21248K)], 0.0062220 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
2.113: [GC [PSYoungGen: 15728K->96K(917504K)] 16020K->387K(1966080K), 0.0006030 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2.114: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 291K->308K(1048576K)] 387K->308K(1966080K) [PSPermGen: 2671K->2671K(21248K)], 0.0042540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
measure...
run (ns): 80120000
run (ms): 80
Learned1_ArrayDequeIterator
2.200: [GC [PSYoungGen: 15728K->96K(917504K)] 16037K->404K(1966080K), 0.0002670 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
2.200: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 308K->291K(1048576K)] 404K->291K(1966080K) [PSPermGen: 2675K->2675K(21248K)], 0.0041930 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
4.225: [GC [PSYoungGen: 15728K->128K(917504K)] 16020K->419K(1966080K), 0.0006660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.225: [Full GC (System) [PSYoungGen: 128K->0K(917504K)] [ParOldGen: 291K->309K(1048576K)] 419K->309K(1966080K) [PSPermGen: 2683K->2683K(21248K)], 0.0050430 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 206036000
run (ms): 206
Learned1_IntegerArray
4.438: [GC [PSYoungGen: 15728K->64K(917504K)] 16038K->373K(1966080K), 0.0003260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.438: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 309K->293K(1048576K)] 373K->293K(1966080K) [PSPermGen: 2687K->2687K(21248K)], 0.0044960 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
4.449: [GC [PSYoungGen: 15732K->96K(917504K)] 16025K->389K(1966080K), 0.0003930 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
4.450: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 293K->310K(1048576K)] 389K->310K(1966080K) [PSPermGen: 2689K->2689K(21248K)], 0.0044650 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 4033000
run (ms): 4
Learned1_intArray
4.460: [GC [PSYoungGen: 15728K->64K(917504K)] 16039K->374K(1966080K), 0.0006510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.461: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 310K->293K(1048576K)] 374K->293K(1966080K) [PSPermGen: 2692K->2692K(21248K)], 0.0051150 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
4.473: [GC [PSYoungGen: 15732K->32K(917504K)] 16026K->325K(1966080K), 0.0002750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.473: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->297K(1048576K)] 325K->297K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0049060 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 4559000
run (ms): 4
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x000000014d3a0000, 0x000000018d3a0000, 0x000000018d3a0000)
  eden space 786432K, 4% used [0x000000014d3a0000,0x000000014f258630,0x000000017d3a0000)
  from space 131072K, 0% used [0x00000001853a0000,0x00000001853a0000,0x000000018d3a0000)
  to   space 131072K, 0% used [0x000000017d3a0000,0x000000017d3a0000,0x00000001853a0000)
 ParOldGen       total 1048576K, used 297K [0x000000010d3a0000, 0x000000014d3a0000, 0x000000014d3a0000)
  object space 1048576K, 0% used [0x000000010d3a0000,0x000000010d3ea500,0x000000014d3a0000)
 PSPermGen       total 21248K, used 2701K [0x00000001081a0000, 0x0000000109660000, 0x000000010d3a0000)
  object space 21248K, 12% used [0x00000001081a0000,0x0000000108443590,0x0000000109660000)

`````

### Output: 10000 Elements
`````
Learned1_ArrayListIterator
0.131: [GC [PSYoungGen: 15728K->448K(917504K)] 15728K->448K(1966080K), 0.0218010 secs] [Times: user=0.04 sys=0.00, real=0.03 secs] 
0.153: [Full GC (System) [PSYoungGen: 448K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 448K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0299910 secs] [Times: user=0.05 sys=0.00, real=0.03 secs] 
start...
test true
warmup...
warmup done
9.594: [GC [PSYoungGen: 47186K->263K(917504K)] 47484K->569K(1966080K), 0.0007680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
9.595: [Full GC (System) [PSYoungGen: 263K->0K(917504K)] [ParOldGen: 306K->488K(1048576K)] 569K->488K(1966080K) [PSPermGen: 2666K->2666K(21248K)], 0.0075400 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 936265000
run (ms): 936
Learned1_ArrayListGet
10.540: [GC [PSYoungGen: 31457K->64K(917504K)] 31945K->552K(1966080K), 0.0005420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
10.541: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 488K->292K(1048576K)] 552K->292K(1966080K) [PSPermGen: 2669K->2669K(21248K)], 0.0061770 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
19.029: [GC [PSYoungGen: 15728K->231K(917504K)] 16020K->523K(1966080K), 0.0007130 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
19.029: [Full GC (System) [PSYoungGen: 231K->0K(917504K)] [ParOldGen: 292K->485K(1048576K)] 523K->485K(1966080K) [PSPermGen: 2672K->2672K(21248K)], 0.0055270 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 801900000
run (ms): 801
Learned1_ArrayDequeIterator
19.839: [GC [PSYoungGen: 15728K->64K(917504K)] 16214K->549K(1966080K), 0.0007310 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
19.840: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 485K->292K(1048576K)] 549K->292K(1966080K) [PSPermGen: 2676K->2676K(21248K)], 0.0047690 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
39.541: [GC [PSYoungGen: 15728K->256K(917504K)] 16021K->548K(1966080K), 0.0014620 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
39.543: [Full GC (System) [PSYoungGen: 256K->0K(917504K)] [ParOldGen: 292K->511K(1048576K)] 548K->511K(1966080K) [PSPermGen: 2684K->2684K(21248K)], 0.0073430 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
measure...
run (ns): 1959797000
run (ms): 1959
Learned1_IntegerArray
41.511: [GC [PSYoungGen: 15728K->96K(917504K)] 16240K->607K(1966080K), 0.0007290 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
41.512: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 511K->293K(1048576K)] 607K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0041110 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
41.523: [GC [PSYoungGen: 15767K->263K(917504K)] 16061K->556K(1966080K), 0.0006460 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
41.524: [Full GC (System) [PSYoungGen: 263K->0K(917504K)] [ParOldGen: 293K->486K(1048576K)] 556K->486K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0056590 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 5415000
run (ms): 5
Learned1_intArray
41.536: [GC [PSYoungGen: 15728K->64K(917504K)] 16215K->550K(1966080K), 0.0015420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
41.538: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 486K->294K(1048576K)] 550K->294K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0048560 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
41.549: [GC [PSYoungGen: 15767K->39K(917504K)] 16061K->333K(1966080K), 0.0006490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
41.550: [Full GC (System) [PSYoungGen: 39K->0K(917504K)] [ParOldGen: 294K->333K(1048576K)] 333K->333K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0041290 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 5488000
run (ms): 5
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x00000001520b0000, 0x00000001920b0000, 0x00000001920b0000)
  eden space 786432K, 4% used [0x00000001520b0000,0x0000000153f68630,0x00000001820b0000)
  from space 131072K, 0% used [0x000000018a0b0000,0x000000018a0b0000,0x00000001920b0000)
  to   space 131072K, 0% used [0x00000001820b0000,0x00000001820b0000,0x000000018a0b0000)
 ParOldGen       total 1048576K, used 333K [0x00000001120b0000, 0x00000001520b0000, 0x00000001520b0000)
  object space 1048576K, 0% used [0x00000001120b0000,0x0000000112103458,0x00000001520b0000)
 PSPermGen       total 21248K, used 2702K [0x000000010ceb0000, 0x000000010e370000, 0x00000001120b0000)
  object space 21248K, 12% used [0x000000010ceb0000,0x000000010d153980,0x000000010e370000)

`````

### Output: 100000 Elements
`````
Learned1_ArrayListIterator
0.135: [GC [PSYoungGen: 15728K->448K(917504K)] 15728K->448K(1966080K), 0.0033000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.138: [Full GC (System) [PSYoungGen: 448K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 448K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0206950 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
warmup done
98.201: [GC [PSYoungGen: 47186K->2022K(917504K)] 47484K->2329K(1966080K), 0.0045620 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
98.206: [Full GC (System) [PSYoungGen: 2022K->0K(917504K)] [ParOldGen: 306K->2246K(1048576K)] 2329K->2246K(1966080K) [PSPermGen: 2666K->2666K(21248K)], 0.0446470 secs] [Times: user=0.07 sys=0.00, real=0.04 secs] 
measure...
run (ns): 9622554000
run (ms): 9622
Learned1_ArrayListGet
107.875: [GC [PSYoungGen: 31457K->96K(917504K)] 33703K->2342K(1966080K), 0.0002870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
107.876: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2246K->292K(1048576K)] 2342K->292K(1966080K) [PSPermGen: 2669K->2669K(21248K)], 0.0058340 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
204.062: [GC [PSYoungGen: 15728K->2022K(917504K)] 16020K->2314K(1966080K), 0.0054920 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
204.068: [Full GC (System) [PSYoungGen: 2022K->0K(917504K)] [ParOldGen: 292K->2243K(1048576K)] 2314K->2243K(1966080K) [PSPermGen: 2672K->2672K(21248K)], 0.0299940 secs] [Times: user=0.06 sys=0.00, real=0.03 secs] 
measure...
run (ns): 9706815000
run (ms): 9706
Learned1_ArrayDequeIterator
213.806: [GC [PSYoungGen: 15728K->96K(917504K)] 17971K->2339K(1966080K), 0.0002640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
213.806: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2243K->292K(1048576K)] 2339K->292K(1966080K) [PSPermGen: 2676K->2676K(21248K)], 0.0043100 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
407.810: [GC [PSYoungGen: 15728K->2176K(917504K)] 16021K->2468K(1966080K), 0.0045080 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
407.815: [Full GC (System) [PSYoungGen: 2176K->0K(917504K)] [ParOldGen: 292K->2365K(1048576K)] 2468K->2365K(1966080K) [PSPermGen: 2684K->2684K(21248K)], 0.0252430 secs] [Times: user=0.07 sys=0.00, real=0.02 secs] 
measure...
run (ns): 19485204000
run (ms): 19485
Learned1_IntegerArray
427.327: [GC [PSYoungGen: 15728K->96K(917504K)] 18094K->2461K(1966080K), 0.0003110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
427.327: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2365K->293K(1048576K)] 2461K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0043160 secs] [Times: user=0.01 sys=0.01, real=0.00 secs] 
start...
test true
warmup...
warmup done
427.345: [GC [PSYoungGen: 16119K->2022K(917504K)] 16413K->2316K(1966080K), 0.0038480 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
427.349: [Full GC (System) [PSYoungGen: 2022K->0K(917504K)] [ParOldGen: 293K->2244K(1048576K)] 2316K->2244K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0233520 secs] [Times: user=0.07 sys=0.00, real=0.02 secs] 
measure...
run (ns): 5170000
run (ms): 5
Learned1_intArray
427.379: [GC [PSYoungGen: 15728K->64K(917504K)] 17973K->2308K(1966080K), 0.0005310 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
427.380: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 2244K->294K(1048576K)] 2308K->294K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0109210 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
427.402: [GC [PSYoungGen: 16119K->390K(917504K)] 16413K->684K(1966080K), 0.0003290 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
427.402: [Full GC (System) [PSYoungGen: 390K->0K(917504K)] [ParOldGen: 294K->684K(1048576K)] 684K->684K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0067650 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 6056000
run (ms): 6
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x0000000152500000, 0x0000000192500000, 0x0000000192500000)
  eden space 786432K, 4% used [0x0000000152500000,0x00000001543b8630,0x0000000182500000)
  from space 131072K, 0% used [0x000000018a500000,0x000000018a500000,0x0000000192500000)
  to   space 131072K, 0% used [0x0000000182500000,0x0000000182500000,0x000000018a500000)
 ParOldGen       total 1048576K, used 684K [0x0000000112500000, 0x0000000152500000, 0x0000000152500000)
  object space 1048576K, 0% used [0x0000000112500000,0x00000001125ab298,0x0000000152500000)
 PSPermGen       total 21248K, used 2702K [0x000000010d300000, 0x000000010e7c0000, 0x0000000112500000)
  object space 21248K, 12% used [0x000000010d300000,0x000000010d5a3980,0x000000010e7c0000)
`````

### Output: 1000000 Elements
`````
`````
