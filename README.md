# Video-Share-Platform
This is a WeChat Mini Program developed by Lingwei Luo  

It is a Video Share Platform which is similar to TikTok  

Overview
---------
* Video App
  * Frontend
    * Language: JavaScript + HTML + CSS

  * Backend
    * Language: Java
    * Framework: Spring Boot
    * Tools: redis, swagger2, ffmpeg, maven

  * Database
    * mysql + mybatis + mybatis-pagehelper
  

* Video Admin
  * Frontend
    * Language: JavaScript + JSP + CSS
    * Framework: Bootstrap + Jquery
    * Tools: jqGrid

  * Backend
    * Language: Java
    * Framework: Spring MVC
    * Tools: zookeeper, maven

  * Database
    * mysql + mybatis + mybatis-pagehelper

Supported modules
---------
* Video App
  * User related:
    * Regist, Login, Logout  
    * ChangeFace
    * Follow (unfollow) user
    * Report user

  * Video related
    * User upload videos
    * User combine video with bgm
    * User like videos
    * User check others' videos
    * User comment on videos
    * User reply to others' comments
    * User repost video to friends
    * User search videos by topic
    * User download video

* Video Admin
  * User related:
    * Admin login
    * User list query
    * Search specific user 

  * Video related:
    * Add bgm  
    * Delete bgm   
    (Client will keep synchronization via zookeeper)
    * User reports list query
    * Prohibit videos



Program presentation
---------

* Video App
---------------------------------------------------

<div align=center>
<img src="Pictures/Video/Swagger2.png" width="850"/>
</div>

---------------------------------------------------
<div align="center">
    <img src="Pictures/Video/IMG_2187.jpg" width="320"/>
    <img src="Pictures/Video/IMG_2188.jpg" width="320"/>
</div>

<div align="center">
    <img src="Pictures/Video/IMG_2189.jpg" width="320"/>
    <img src="Pictures/Video/IMG_2190.jpg" width="320"/>
</div>

<div align="center">
    <img src="Pictures/Video/IMG_2192.jpg" width="320"/>
    <img src="Pictures/Video/IMG_2193.jpg" width="320"/>
</div>

<div align="center">
    <img src="Pictures/Video/IMG_2191.jpg" width="320"/>
    <img src="Pictures/Video/IMG_2195.jpg" width="320"/>
</div>

<div align="center">
    <img src="Pictures/Video/IMG_2185.jpg" width="320"/>
    <img src="Pictures/Video/IMG_2194.jpg" width="320"/>
</div>

---------------------------------------------------

* Video Admin

---------------------------------------------------

<div align="center">
    <img src="Pictures/Admin/01.png" width="850"/>
</div>

<div align="center">
    <img src="Pictures/Admin/02.png" width="850"/>
</div>

<div align="center">
    <img src="Pictures/Admin/03.png" width="850"/>
</div>

<div align="center">
    <img src="Pictures/Admin/04.png" width="850"/>
</div>

<div align="center">
    <img src="Pictures/Admin/05.png" width="850"/>
</div>

<div align="center">
    <img src="Pictures/Admin/06.png" width="850"/>
</div>