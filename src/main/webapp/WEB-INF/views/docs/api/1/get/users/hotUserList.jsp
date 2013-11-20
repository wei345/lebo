<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="红人榜按钮设置 & 推荐用户" method="GET" action="${ctx}/api/1/users/hotUserList.json">
    <p>
        true - 返回结果带有按钮设置，false - 返回结果不带按钮设置，默认false。
    </p>
    <tags:field name="btn" optional="true"/>
    <tags:fields-page-size/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/users/hotUserList.json?btn=true">
    {
        hotuser_button1_backgroundColor: "#7E5CDA",
        hotuser_button1_imageUrl: "http://file.lebooo.com/images/hotuser/btn1.png",
        hotuser_button1_text: "粉丝最多",
        hotuser_button2_backgroundColor: "#D67E89",
        hotuser_button2_imageUrl: "http://file.lebooo.com/images/hotuser/btn2.png",
        hotuser_button2_text: "最受喜欢",
        hotuser_button3_backgroundColor: "#30B5F0",
        hotuser_button3_imageUrl: "http://file.lebooo.com/images/hotuser/btn3.png",
        hotuser_button3_text: "导演排行",
        users: [
            {
                id: "51ce72850cf28c89d0d250ab",
                screenName: "認真de-磊",
                description: "大家的支持！就是我的动力！谢谢大家的❤关注我，我必回粉！",
                profileImageUrl: "http://file.lebooo.com/user/2013-06-29/51ce72850cf28c89d0d250ab-normal-7073.png",
                profileImageBiggerUrl: "http://file.lebooo.com/user/2013-06-29/51ce72850cf28c89d0d250ab-bigger-26690.png",
                profileImageOriginalUrl: "http://file.lebooo.com/user/2013-06-29/51ce72850cf28c89d0d250ab-original-653383.png",
                createdAt: "Sat Jun 29 13:37:09 +0800 2013",
                following: false,
                followersCount: 1099,
                friendsCount: 38936,
                statusesCount: 101,
                favoritesCount: 1951,
                beFavoritedCount: 3072,
                viewCount: 52950,
                digestCount: 13,
                hotBeFavoritedCount: 1,
                blocking: false,
                bilateral: false,
                level: 4
            },
            {
                id: "51e8d7130cf28c89d0d22f53",
                screenName: "闫涛",
                description: "闫涛",
                profileImageUrl: "http://file.lebooo.com/user/2013-07-19/51e8d7130cf28c89d0d22f53-normal-5013.png",
                profileImageBiggerUrl: "http://file.lebooo.com/user/2013-07-19/51e8d7130cf28c89d0d22f53-bigger-19272.png",
                profileImageOriginalUrl: "http://file.lebooo.com/user/2013-07-19/51e8d7130cf28c89d0d22f53-original-19272.png",
                createdAt: "Fri Jul 19 14:05:07 +0800 2013",
                following: false,
                followersCount: 4,
                friendsCount: 5,
                statusesCount: 4,
                favoritesCount: 1,
                beFavoritedCount: 1,
                viewCount: 6,
                digestCount: 0,
                hotBeFavoritedCount: 1,
                blocking: false,
                bilateral: false,
                level: 0
            }
        ]
    }
</tags:example>