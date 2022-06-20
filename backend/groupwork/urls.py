"""groupwork URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from db.views import *
from django.views.static import serve
from django.conf import settings
from django.conf.urls.static import static
urlpatterns = [
    path('admin/', admin.site.urls),
    path('filetest/',file_test),
    path(r'media/(?P<path>.*)',serve,{'document_root':settings.MEDIA_ROOT}),
    path('user/login/',user_login),
    path('user/register/',user_register),
    path('user/update/',user_update),
    path('user/follow/',follow),
    path('user/get/',user_get),
    path('user/follow/delete/',delete_follow),
    path('user/ban/',ban),
    path('user/ban/delete/',delete_ban),
    path('item/send/',item_send),
    path('item/delete/',item_delete),
    path('item/like/',like),
    path('item/dislike/',delete_like),
    path('item/get/',get_item),
    path('item/comment/',get_comment),
    path('comment/send/',comment_send),
    path('comment/delete/',comment_delete),
    path('get/item/',get_item),
    path('get/self_item/',get_self_item),
    path('get/notice/',get_notice)
]+ static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
