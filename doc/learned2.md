# Learned: Iterating over Integer[] vs int[]

## Summary

I found some interesting performance differences while solving the [timeseries-chalenge](https://github.com/cinovo/timeseries-challenge).

## Expectation
* I expected that iterating over a primitive Array int[] is faster than Integer[] because we can benefit from cpu caches

## Benchmark

`````
java version "1.7.0_07"
Java(TM) SE Runtime Environment (build 1.7.0_07-b10)
Java HotSpot(TM) 64-Bit Server VM (build 23.3-b01, mixed mode)
`````

* VM arguments: -Xmx2G -Xms2G -Xmn1G -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails -XX:+UseCompressedOops
* call System.gc() to remove garbage from previous run
* iterating 1 mio times over the data structure to warm up the VM.
* call System.gc() to remove garbage from warmup
* begin = System.nanoTime()
* iteration 100000 times over the data structure
* end = System.nanoTime()
* runtime = end-begin

### Result

<table>
	<thead>
		<tr>
			<th>Implementation</th>
			<th>1 Elements</th>
			<th>10 Elements</th>
			<th>100 Elements</th>
			<th>1000 Elements</th>
			<th>10000 Elements</th>
			<th>100000 Elements</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Learned2_IntegerArray</td>
			<td>5</td>
			<td>7</td>
			<td>8</td>
			<td>47</td>
			<td>433</td>
			<td>4204</td>
		</tr>
		<tr>
			<td>Learned2_intArray</td>
			<td>5</td>
			<td>5</td>
			<td>9</td>
			<td>15</td>
			<td>77</td>
			<td>558</td>
		</tr>
	</tbody>
</table>

## Conclusions

* I expected that iterating over a primitive Array int[] is faster than Integer[] because we can benefit from cpu caches
	* yes :)

## Log

### Output: 1 Elements
`````
Learned2_IntegerArray
0.114: [GC [PSYoungGen: 15728K->368K(917504K)] 15728K->368K(1966080K), 0.0026810 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
0.117: [Full GC (System) [PSYoungGen: 368K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 368K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0127300 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.141: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->402K(1966080K), 0.0003880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.142: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 306K->292K(1048576K)] 402K->292K(1966080K) [PSPermGen: 2658K->2658K(21248K)], 0.0063480 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 5726000
run (ms): 5
Learned2_intArray
0.155: [GC [PSYoungGen: 31457K->64K(917504K)] 31750K->356K(1966080K), 0.0003490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.156: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->289K(1048576K)] 356K->289K(1966080K) [PSPermGen: 2661K->2661K(21248K)], 0.0085030 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.172: [GC [PSYoungGen: 15728K->32K(917504K)] 16018K->321K(1966080K), 0.0002870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.172: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 289K->289K(1048576K)] 321K->289K(1966080K) [PSPermGen: 2662K->2662K(21248K)], 0.0049690 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 5945000
run (ms): 5
all done
Heap
 PSYoungGen      total 917504K, used 23593K [0x000000014ca00000, 0x000000018ca00000, 0x000000018ca00000)
  eden space 786432K, 3% used [0x000000014ca00000,0x000000014e10a4e8,0x000000017ca00000)
  from space 131072K, 0% used [0x0000000184a00000,0x0000000184a00000,0x000000018ca00000)
  to   space 131072K, 0% used [0x000000017ca00000,0x000000017ca00000,0x0000000184a00000)
 ParOldGen       total 1048576K, used 289K [0x000000010ca00000, 0x000000014ca00000, 0x000000014ca00000)
  object space 1048576K, 0% used [0x000000010ca00000,0x000000010ca48770,0x000000014ca00000)
 PSPermGen       total 21248K, used 2670K [0x0000000107800000, 0x0000000108cc0000, 0x000000010ca00000)
  object space 21248K, 12% used [0x0000000107800000,0x0000000107a9b998,0x0000000108cc0000)

`````

### Output: 10 Elements
`````
Learned2_IntegerArray
0.116: [GC [PSYoungGen: 15728K->432K(917504K)] 15728K->432K(1966080K), 0.0015570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.118: [Full GC (System) [PSYoungGen: 432K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 432K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0079950 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.147: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->402K(1966080K), 0.0003460 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.147: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 306K->293K(1048576K)] 402K->293K(1966080K) [PSPermGen: 2658K->2658K(21248K)], 0.0070800 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 7508000
run (ms): 7
Learned2_intArray
0.164: [GC [PSYoungGen: 31457K->64K(917504K)] 31750K->357K(1966080K), 0.0005060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.164: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->289K(1048576K)] 357K->289K(1966080K) [PSPermGen: 2661K->2661K(21248K)], 0.0081420 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.181: [GC [PSYoungGen: 15728K->32K(917504K)] 16018K->321K(1966080K), 0.0003080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.181: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 289K->289K(1048576K)] 321K->289K(1966080K) [PSPermGen: 2662K->2662K(21248K)], 0.0049260 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 5185000
run (ms): 5
all done
Heap
 PSYoungGen      total 917504K, used 23593K [0x000000014fd90000, 0x000000018fd90000, 0x000000018fd90000)
  eden space 786432K, 3% used [0x000000014fd90000,0x000000015149a4e8,0x000000017fd90000)
  from space 131072K, 0% used [0x0000000187d90000,0x0000000187d90000,0x000000018fd90000)
  to   space 131072K, 0% used [0x000000017fd90000,0x000000017fd90000,0x0000000187d90000)
 ParOldGen       total 1048576K, used 289K [0x000000010fd90000, 0x000000014fd90000, 0x000000014fd90000)
  object space 1048576K, 0% used [0x000000010fd90000,0x000000010fdd8790,0x000000014fd90000)
 PSPermGen       total 21248K, used 2670K [0x000000010ab90000, 0x000000010c050000, 0x000000010fd90000)
  object space 21248K, 12% used [0x000000010ab90000,0x000000010ae2b998,0x000000010c050000)

`````

### Output: 100 Elements
`````
Learned2_IntegerArray
0.124: [GC [PSYoungGen: 15728K->384K(917504K)] 15728K->384K(1966080K), 0.0109300 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
0.135: [Full GC (System) [PSYoungGen: 384K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 384K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0147570 secs] [Times: user=0.04 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.216: [GC [PSYoungGen: 47186K->128K(917504K)] 47484K->434K(1966080K), 0.0002660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.216: [Full GC (System) [PSYoungGen: 128K->0K(917504K)] [ParOldGen: 306K->293K(1048576K)] 434K->293K(1966080K) [PSPermGen: 2658K->2658K(21248K)], 0.0053060 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
measure...
run (ns): 8327000
run (ms): 8
Learned2_intArray
0.231: [GC [PSYoungGen: 31457K->64K(917504K)] 31750K->357K(1966080K), 0.0003720 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.232: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->289K(1048576K)] 357K->289K(1966080K) [PSPermGen: 2661K->2661K(21248K)], 0.0079220 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.250: [GC [PSYoungGen: 15729K->32K(917504K)] 16019K->321K(1966080K), 0.0004090 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.251: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 289K->290K(1048576K)] 321K->290K(1966080K) [PSPermGen: 2662K->2662K(21248K)], 0.0065640 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 9966000
run (ms): 9
all done
Heap
 PSYoungGen      total 917504K, used 23593K [0x0000000152b20000, 0x0000000192b20000, 0x0000000192b20000)
  eden space 786432K, 3% used [0x0000000152b20000,0x000000015422a4a0,0x0000000182b20000)
  from space 131072K, 0% used [0x000000018ab20000,0x000000018ab20000,0x0000000192b20000)
  to   space 131072K, 0% used [0x0000000182b20000,0x0000000182b20000,0x000000018ab20000)
 ParOldGen       total 1048576K, used 290K [0x0000000112b20000, 0x0000000152b20000, 0x0000000152b20000)
  object space 1048576K, 0% used [0x0000000112b20000,0x0000000112b688f8,0x0000000152b20000)
 PSPermGen       total 21248K, used 2670K [0x000000010d920000, 0x000000010ede0000, 0x0000000112b20000)
  object space 21248K, 12% used [0x000000010d920000,0x000000010dbbb998,0x000000010ede0000)

`````

### Output: 1000 Elements
`````
Learned2_IntegerArray
0.118: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0018430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.120: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0116670 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.598: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->394K(1966080K), 0.0008110 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.599: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 298K->310K(1048576K)] 394K->310K(1966080K) [PSPermGen: 2658K->2658K(21248K)], 0.0060140 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 47404000
run (ms): 47
Learned2_intArray
0.654: [GC [PSYoungGen: 31457K->64K(917504K)] 31767K->374K(1966080K), 0.0003330 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.654: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 310K->289K(1048576K)] 374K->289K(1966080K) [PSPermGen: 2661K->2661K(21248K)], 0.0084630 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
0.679: [GC [PSYoungGen: 15732K->32K(917504K)] 16022K->321K(1966080K), 0.0006760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.680: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 289K->293K(1048576K)] 321K->293K(1966080K) [PSPermGen: 2663K->2663K(21248K)], 0.0042280 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 15779000
run (ms): 15
all done
Heap
 PSYoungGen      total 917504K, used 23593K [0x0000000158660000, 0x0000000198660000, 0x0000000198660000)
  eden space 786432K, 3% used [0x0000000158660000,0x0000000159d6a4a0,0x0000000188660000)
  from space 131072K, 0% used [0x0000000190660000,0x0000000190660000,0x0000000198660000)
  to   space 131072K, 0% used [0x0000000188660000,0x0000000188660000,0x0000000190660000)
 ParOldGen       total 1048576K, used 293K [0x0000000118660000, 0x0000000158660000, 0x0000000158660000)
  object space 1048576K, 0% used [0x0000000118660000,0x00000001186a9708,0x0000000158660000)
 PSPermGen       total 21248K, used 2670K [0x0000000113460000, 0x0000000114920000, 0x0000000118660000)
  object space 21248K, 12% used [0x0000000113460000,0x00000001136fba40,0x0000000114920000)

`````

### Output: 10000 Elements
`````
Learned2_IntegerArray
0.113: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0021180 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
0.116: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0180530 secs] [Times: user=0.04 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
4.423: [GC [PSYoungGen: 47186K->295K(917504K)] 47484K->601K(1966080K), 0.0010560 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
4.424: [Full GC (System) [PSYoungGen: 295K->0K(917504K)] [ParOldGen: 306K->487K(1048576K)] 601K->487K(1966080K) [PSPermGen: 2659K->2659K(21248K)], 0.0063630 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 433013000
run (ms): 433
Learned2_intArray
4.865: [GC [PSYoungGen: 31457K->64K(917504K)] 31944K->551K(1966080K), 0.0003410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.865: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 487K->290K(1048576K)] 551K->290K(1966080K) [PSPermGen: 2662K->2662K(21248K)], 0.0057430 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
4.940: [GC [PSYoungGen: 15767K->39K(917504K)] 16058K->329K(1966080K), 0.0002900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.941: [Full GC (System) [PSYoungGen: 39K->0K(917504K)] [ParOldGen: 290K->329K(1048576K)] 329K->329K(1966080K) [PSPermGen: 2663K->2663K(21248K)], 0.0041750 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 77628000
run (ms): 77
all done
Heap
 PSYoungGen      total 917504K, used 23593K [0x0000000152cc0000, 0x0000000192cc0000, 0x0000000192cc0000)
  eden space 786432K, 3% used [0x0000000152cc0000,0x00000001543ca4a0,0x0000000182cc0000)
  from space 131072K, 0% used [0x000000018acc0000,0x000000018acc0000,0x0000000192cc0000)
  to   space 131072K, 0% used [0x0000000182cc0000,0x0000000182cc0000,0x000000018acc0000)
 ParOldGen       total 1048576K, used 329K [0x0000000112cc0000, 0x0000000152cc0000, 0x0000000152cc0000)
  object space 1048576K, 0% used [0x0000000112cc0000,0x0000000112d12768,0x0000000152cc0000)
 PSPermGen       total 21248K, used 2671K [0x000000010dac0000, 0x000000010ef80000, 0x0000000112cc0000)
  object space 21248K, 12% used [0x000000010dac0000,0x000000010dd5bd00,0x000000010ef80000)

`````

### Output: 100000 Elements
`````
Learned2_IntegerArray
0.109: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0103160 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
0.119: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2652K->2650K(21248K)], 0.0066480 secs] [Times: user=0.01 sys=0.01, real=0.01 secs] 
start...
test false
warmup...
warmup done
42.930: [GC [PSYoungGen: 47186K->1990K(917504K)] 47484K->2297K(1966080K), 0.0060560 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
42.936: [Full GC (System) [PSYoungGen: 1990K->0K(917504K)] [ParOldGen: 306K->2245K(1048576K)] 2297K->2245K(1966080K) [PSPermGen: 2659K->2659K(21248K)], 0.0246350 secs] [Times: user=0.07 sys=0.01, real=0.03 secs] 
measure...
run (ns): 4204842000
run (ms): 4204
Learned2_intArray
47.167: [GC [PSYoungGen: 31457K->96K(917504K)] 33702K->2341K(1966080K), 0.0002800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
47.167: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2245K->290K(1048576K)] 2341K->290K(1966080K) [PSPermGen: 2662K->2662K(21248K)], 0.0056580 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test false
warmup...
warmup done
47.721: [GC [PSYoungGen: 16119K->390K(917504K)] 16410K->681K(1966080K), 0.0006670 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
47.722: [Full GC (System) [PSYoungGen: 390K->0K(917504K)] [ParOldGen: 290K->681K(1048576K)] 681K->681K(1966080K) [PSPermGen: 2663K->2663K(21248K)], 0.0040380 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
measure...
run (ns): 558474000
run (ms): 558
all done
Heap
 PSYoungGen      total 917504K, used 23593K [0x0000000158960000, 0x0000000198960000, 0x0000000198960000)
  eden space 786432K, 3% used [0x0000000158960000,0x000000015a06a4a0,0x0000000188960000)
  from space 131072K, 0% used [0x0000000190960000,0x0000000190960000,0x0000000198960000)
  to   space 131072K, 0% used [0x0000000188960000,0x0000000188960000,0x0000000190960000)
 ParOldGen       total 1048576K, used 681K [0x0000000118960000, 0x0000000158960000, 0x0000000158960000)
  object space 1048576K, 0% used [0x0000000118960000,0x0000000118a0a5a8,0x0000000158960000)
 PSPermGen       total 21248K, used 2671K [0x0000000113760000, 0x0000000114c20000, 0x0000000118960000)
  object space 21248K, 12% used [0x0000000113760000,0x00000001139fbcf0,0x0000000114c20000)

`````


