from django.db import models
from django.forms import CharField, IntegerField
from numpy import integer
class filetest(models.Model):
    file=models.FileField()
class user(models.Model):
    user_name=models.CharField(max_length=50)
    password=models.CharField(max_length=50)
    email=models.CharField(max_length=50)
    user_image_name=models.FileField()
    introduction=models.CharField(max_length=200)
    account_birth=models.DateTimeField(auto_now_add=True)
class item(models.Model):
    user_id=models.IntegerField()
    content=models.CharField(max_length=200)
    title=models.CharField(max_length=200)
    type=models.IntegerField()
    location=models.CharField(max_length=200)
    created_time=models.DateTimeField(auto_now_add=True)
    like_account=models.IntegerField(default=0)
    comment_account=models.IntegerField(default=0)
    file=models.FileField()
    fake_image=models.FileField(null=True,default=None)
class user_comment(models.Model):
    item_id=models.IntegerField()
    user_id=models.IntegerField()
    content=models.CharField(max_length=200)
    created_time=models.DateTimeField(auto_now_add=True)
class user_like(models.Model):
    item_id=models.IntegerField()
    user_id=models.IntegerField()
class user_follow(models.Model):
    src_user_id=models.IntegerField()
    dst_user_id=models.IntegerField()
class user_ban(models.Model):
    src_user_id=models.IntegerField()
    dst_user_id=models.IntegerField()
class user_notice(models.Model):
    src_user_id=models.IntegerField()
    dst_user_id=models.IntegerField()
    created_time=models.DateTimeField(auto_now_add=True)
    content=models.CharField(max_length=200)
    type=models.CharField(max_length=20)
    item_id=models.IntegerField()
# Create your models here.
