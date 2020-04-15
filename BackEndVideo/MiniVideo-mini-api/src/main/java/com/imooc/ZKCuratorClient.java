package com.imooc;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import com.imooc.cofig.ResourceConfig;
import com.imooc.enums.BGMOperatorTypeEnum;
import com.imooc.utils.JsonUtils;

@Component
public class ZKCuratorClient {

	// zk客户端
	private CuratorFramework client = null;	
	final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

//	@Autowired
//	private BgmService bgmService;
	
	public static final String ZOOKEEPER_SERVER = "192.168.1.178:2181";

	
	public void init() {
		
		if (client != null) {
			return;
		}

		// 重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);
		client = CuratorFrameworkFactory.builder()
				.connectString(ZOOKEEPER_SERVER)
				.sessionTimeoutMs(10000)
				.retryPolicy(retryPolicy)
				.namespace("admin")
				.build();
		client.start();
		try {
//			String testNodeData = new String(client.getData().forPath("/bgm/200414HBKHCHFGF8"));
//			log.info("测试的节点数据为: {}",testNodeData);
			addChildWatch("/bgm");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addChildWatch(String nodePath) throws Exception {

		final PathChildrenCache cache = new PathChildrenCache(client,nodePath,true);
		cache.start();
		cache.getListenable().addListener(new PathChildrenCacheListener() {
			@Override
			public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
				if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)){
					log.info("监听到事件CHILD_ADDED");
				}
			}
		});

	}
	
}
