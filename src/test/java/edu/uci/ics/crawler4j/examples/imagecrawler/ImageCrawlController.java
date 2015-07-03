/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.examples.imagecrawler;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.authentication.AuthInfo;
import edu.uci.ics.crawler4j.crawler.authentication.FormAuthInfo;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Yasser Ganjisaffar
 */
public class ImageCrawlController {
  private static final Logger logger = LoggerFactory.getLogger(ImageCrawlController.class);

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      logger.info("Needed parameters: ");
      logger.info("\t rootFolder (it will contain intermediate crawl data)");
      logger.info("\t numberOfCralwers (number of concurrent threads)");
      logger.info("\t storageFolder (a folder for storing downloaded images)");
      return;
    }

    String rootFolder = args[0];
    int numberOfCrawlers = Integer.parseInt(args[1]);
    String storageFolder = args[2];

    CrawlConfig config = new CrawlConfig();

    config.setCrawlStorageFolder(rootFolder);

    /*
     * Since images are binary content, we need to set this parameter to
     * true to make sure they are included in the crawl.
     */
    config.setIncludeBinaryContentInCrawling(true);
    
    
    ArrayList<AuthInfo> arrayList = new ArrayList<AuthInfo>();
    HashMap hashMap = new HashMap();
    hashMap.put("mode", "login");
    hashMap.put("return_to", "http://www.pixiv.net/");
    hashMap.put("skip", "1");
    arrayList.add(new FormAuthInfo("hsjab2312@hotmail.com", "XXXX", "https://www.secure.pixiv.net/login.php", "pixiv_id", "pass",hashMap));
    config.setAuthInfos(arrayList);

//    String[] crawlDomains = {"http://bcy.net/illust","http://uci.edu/"};
    
   String[] crawlDomains = {
		   "http://i1.pixiv.net/",
		   "http://i2.pixiv.net/",
		   "http://i3.pixiv.net/",
		   "http://i4.pixiv.net/",
		   "http://www.pixiv.net/"
		   };
    

    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
    
//    for (String domain : crawlDomains) {
//      controller.addSeed(domain);
//    }
    controller.addSeed("http://www.pixiv.net/member_illust.php?mode=medium&illust_id=51160511");

    ImageCrawler.configure(crawlDomains, storageFolder);

    controller.start(ImageCrawler.class, numberOfCrawlers);
  }
}