package com.sunfy.dojo;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拜占庭将军问题(口头协议算法)
 *
 */
public class Byzantine {

	public static void main(String[] args) {
		//将军忠，1叛徒的情况
		//testCase(true, 1);//将军为忠，共1叛徒
		testCase(false, 1);//将军为叛，共1叛徒
		//testCase(true, 2);//将军为忠，共2叛徒
		//testCase(false, 2);//将军为叛，共2叛徒
		//testCase(true, 3);//将军为忠，共3叛徒
		testCase(false, 3);//将军为叛，共3叛徒

	}
	
	/**
	 * 1个测试用例入口
	 * @param isGeneralLoyal 将军是否为忠
	 * @param m 叛徒的数量
	 */
	public static void testCase (boolean isGeneralLoyal, int m) {
		int n = 3 * m;//本用例始终采用 3m+1模型
		List<Executor> exes = genExecutors(n, isGeneralLoyal ? m : m-1);
		Executor currentGeneral = new Executor(0, !isGeneralLoyal);
		Map<Integer, String> cmdList = genCommand("A", currentGeneral, exes);
		Map<Integer, String> result = om(cmdList, exes, m, currentGeneral);
		System.out.println("Initial:" + cmdList + ", Final Result:" + result);
		System.out.println("--------------------------");
	}
	
	/**
	 * 生成执行者的列表。并标记为最后m个执行者为叛徒。
	 * @param n 执行者的数目
	 * @param m 叛徒的数目。
	 * @return 执行者的List
	 */
	private static List<Executor> genExecutors(int n, int m) {
		List<Executor> exes = new ArrayList<Executor>(n);
		for (int i = 1; i <= n; i++) {
			Executor exe = new Executor(i, (i>n-m));
			exes.add(exe);
		}
		return exes;
	}

	/**
	 * OM(m)算法。
	 * @param commands 将军发送给每个副官的命令map
	 * @param exes 副官列表
	 * @param m 叛徒数
	 * @param orgGeneral 当前将军
	 * @return 每个副官的决定
	 */
	public static Map<Integer, String> om(Map<Integer, String> commands, List<Executor> exes, int m, Executor orgGeneral) {
		if (m == 0) {//因为没有叛徒，直接执行命令
			return commands;
		}
		
		Map<?, ?>[] results = new Map[exes.size() + 1];
		results[0] = commands;
		
		//遍历每个副官，将每个副官作为将军，执行OM(m-1)算法，将命令发送给其余n-2个人
		int i = 1;
		for (Executor exe : exes) {
			Executor nextGeneral = exe;//OM(m-1)的将军
			List<Executor> followers = getFollowers(exes, nextGeneral);//OM(m-1)的副官列表
			Map<Integer, String> genCmds = genCommand(commands.get(exe.id), nextGeneral, followers);//OM(m-1)中，该将军发送给每个副官的命令
			results[i++] = om(genCmds, followers, m-1, nextGeneral);//执行OM(m-1)，并汇集结果
		}
		
		//为每个副官分别计算其（V1...Vn）,并找出数量最大的命令
		Map<Integer, String> mergeResults = new HashMap<Integer, String>();
		for (Executor exe : exes) {
			Map<String, Integer> cmds = new HashMap<String, Integer>();//记录每个命令出现的次数
			for (Map<?,?> result : results) {//统计命令出现的次数
				String value = (String) result.get(exe.id);
				if (value == null) continue;
				Integer count = cmds.get(value);
				if (count == null) {
					cmds.put(value, 1);
				} else {
					++count;
					cmds.put(value, count);
				}
			}
			
			//print result
			System.out.println(MessageFormat.format("OM({0}) 将军[{3}],副官[{1}]: {2}",
					m, exe, cmds, orgGeneral));
			//找出出现次数最大的命令，并作为本副官的最终执行命令
			Integer c1 = cmds.get("A");
			Integer c2 = cmds.get("R");
			int cA = c1 == null ? 0 : c1;
			int cR = c2 == null ? 0 : c2;
			String finalCmd = (cR >= cA) ? "R" : "A";
			mergeResults.put(exe.id, finalCmd);
		}
		
		return mergeResults;
	}
	
	/**
	 * 为OM(m-1)生成新的副官列表。即将当前将军从副官列表中剔除。
	 * @param exes 原始的副官列表
	 * @param currentGeneral 当前新选的将军
	 * @return 新的副官列表
	 */
	private static List<Executor> getFollowers(List<Executor> exes, Executor currentGeneral) {
		List<Executor> list = new ArrayList<Executor>(exes);
		list.remove(currentGeneral);
		return list;
	}
	
	/**
	 * 生成发给每一个副官的命令，将结果存入map对象。key为副官的id，值为所发送的命令。
	 * 如果当前将军为忠的话，则如实地将原始命令发送给每个副官，否则，则将发送错误的命令给一部分副官。
	 * @param orgCmd 原始命令
	 * @param currentGeneral 当前将军
	 * @param followers 副官列表
	 * @return 每个副官对应的命令所组成的map对象
	 */
	private static Map<Integer, String> genCommand(String orgCmd, Executor currentGeneral, List<Executor> followers) {
		Map<Integer, String> genCmds = new HashMap<Integer, String>(followers.size());
		if (!currentGeneral.isTrai) {
			for (Executor follower : followers) {
				genCmds.put(follower.id, orgCmd);
			}
		} else {
			int i = 0;
			for (Executor follower : followers) {
				genCmds.put(follower.id, i%2==0 ? wrongCmd(orgCmd) : orgCmd);
				//genCmds.put(follower.id, wrongCmd(orgCmd));
				i++;
			}
		}
		
		return genCmds;
	}
	
	/**
	 * 生成与指定的命令相反的命令。为了简化，只考虑R和A两种命令的情况
	 * @param cmd 指定命令
	 * @return 错误的命令
	 */
	private static String wrongCmd(String cmd) {
		return cmd.equals("A") ? "R" : "A";
	}
	
	/**
	 * 执行者类，包含执行者的id，以及是否为叛徒
	 *
	 */
	static class Executor {
		/**
		 * 执行者id
		 */
		int id;
		/**
		 * 是否为叛徒
		 */
		boolean isTrai;
		public Executor(int id, boolean isTrail) {
			this.id = id;
			this.isTrai = isTrail;
		}
		
		public String toString() {
			return id + "," + (isTrai ? "叛" : "忠");
		}
	}

}
