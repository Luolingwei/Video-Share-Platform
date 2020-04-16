package com.imooc.service.web.util;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKCurator {

	// zk客户端
	private CuratorFramework client = null;

	final static Logger log = LoggerFactory.getLogger(ZKCurator.class);

	public ZKCurator(CuratorFramework client){
		this.client = client;
	}

	public void init(){
		client = client.usingNamespace("admin");

		try {
			// 判断在admin命名空间下有bgm节点 /admin/bgm
			if (client.checkExists().forPath("/bgm")==null){
				/**
				 * 对zk来讲，有两种类型的节点，一种是持久节点，一种是临时节点
				 * 持久节点: 创建一个节点时，节点永远存在，除非手动删除
				 * 临时节点: 创建节点之后，如果会话断开，临时节点自动删除，也可以手动删除
				 */
				client.create().creatingParentsIfNeeded()
						.withMode(CreateMode.PERSISTENT)  // 节点类型: 持久节点
						.withACL(Ids.OPEN_ACL_UNSAFE)  //  acl: 匿名权限
						.forPath("/bgm");
				log.info("zookeepper客户端初始化成功");
				log.info("zookeepper服务器状态: {}",client.isStarted());
			}
		} catch (Exception e) {
			log.error("zookeepper客户端连接、初始化错误");
			e.printStackTrace();
		}

	}

	/**
	 * 增加或者删除bgm, 向zk-server创建子节点，供小程序后端监听
	 */
	public void sendBgmOperator(String bgmId, String operObj){
		try {
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.PERSISTENT)  // 节点类型: 持久节点
					.withACL(Ids.OPEN_ACL_UNSAFE)  //  acl: 匿名权限
					.forPath("/bgm/" + bgmId, operObj.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
