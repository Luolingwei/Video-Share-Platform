package com.llingwei;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.llingwei.config.ResourceConfig;
import com.llingwei.controller.BasicController;
import com.llingwei.service.BgmService;
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
import com.llingwei.enums.BGMOperatorTypeEnum;
import com.llingwei.utils.JsonUtils;

@Component
public class ZKCuratorClient extends BasicController {

	// zk客户端
	private CuratorFramework client = null;	
	final static Logger log = LoggerFactory.getLogger(ZKCuratorClient.class);

	@Autowired
	private BgmService bgmService;

	@Autowired
	private ResourceConfig resourceConfig;

	// 在资源文件 resource.properties中配置
//	public static final String ZOOKEEPER_SERVER = "192.168.1.178:2181";

	
	public void init() {
		
		if (client != null) {
			return;
		}

		// 重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,5);
		client = CuratorFrameworkFactory.builder()
				.connectString(resourceConfig.getZookeeperServer())
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
					log.info("监听到zookeeper事件 CHILD_ADDED");
					// 1 从数据库查询bgm对象，获取bgm的相对路径path
					String zkpath = event.getData().getPath(); // zookeeper的节点路径
					String operatorObjStr = new String(event.getData().getData()); // zookeeper的节点数据，为String类型的map, 包含事件类型, bgmPath
					Map<String, String> map = JsonUtils.jsonToPojo(operatorObjStr, Map.class);
					String operatorType = map.get("operType");
					String bgmPath = map.get("path");

					// 不需要从数据库查path了
//					String arr[] = zkpath.split("/");
//					String bgmId = arr[arr.length-1];
//					Bgm bgm = bgmService.queryBgmById(bgmId);
//					if (bgm == null){
//						return;
//					}

					// 解决中文歌曲名
					String songPath = bgmPath;
					String[] patharr = songPath.split("/");
					String encodeSongpath = "";
					for (String spath: patharr){
						if (StringUtils.isNotBlank(spath)){
							encodeSongpath += "/";
							encodeSongpath += URLEncoder.encode(spath,"UTF-8");
						}
					}

					// 2 定义保存(删除)到本地的bgm路径
					String finalPath = FILE_SPACE + songPath;

					// 3 定义下载的路径(播放url), 这里直接从SpringBoot虚拟目录下载(SpringMVC虚拟目录未配置，我把SpringMVC后台的数据和SpringBoot的数据放在了一起)
					String downloadPath = resourceConfig.getAppServerUrl() + "/mvc-bgm" + encodeSongpath;

					// 4 下载(删除)bgm到SpringBoot服务器
					if (operatorType.equals(BGMOperatorTypeEnum.ADD.type)){
						URL url = new URL(downloadPath);
						File file = new File(finalPath);
						FileUtils.copyURLToFile(url,file);
						// 事件处理完毕，删除zk节点
						client.delete().forPath(zkpath);
					} else if (operatorType.equals(BGMOperatorTypeEnum.DELETE.type)){
						File file = new File(finalPath);
						FileUtils.forceDelete(file);
						// 事件处理完毕，删除zk节点
						client.delete().forPath(zkpath);
					}

				}
			}
		});

	}
	
}
