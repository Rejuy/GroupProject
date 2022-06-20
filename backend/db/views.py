from telnetlib import STATUS
from django.shortcuts import render
from db.models import *
from django.http import HttpResponse,JsonResponse
import json
from fuzzywuzzy import fuzz
import time
def file_test(request):
    pic_obj = request.FILES.get('picture')
    a=filetest()
    a.file=pic_obj
    a.save()
    return JsonResponse({"status":"BS.200","msg":"query articles sucess."})
# Create your views here.
def user_login(request):
    json_param=json.loads( request.body.decode())
    try:
        item=user.objects.get(user_name=json_param['user_name'],password=json_param['password'])
    except:
        try:
            item=user.objects.get(email=json_param['user_name'],password=json_param['password'])
        except:
            item1=user.objects.filter(user_name=json_param['user_name'])
            item2=user.objects.filter(email=json_param['user_name'])
            if len(item1)>0 or len(item2)>0:
                return JsonResponse(status=400,data={"status":"BS.400","msg":"wrong password"})
            return JsonResponse(status=400,data={"status":"BS.400","msg":"user not found"})
        else:
            return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":{"id":item.id}})
    else:
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":{"id":item.id}})
def user_get(request):
    json_param=json.loads( request.body.decode())
    try:
        item=user.objects.get(id=json_param['user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"user not found"})
    else:
        src_follower_id=user_follow.objects.filter(src_user_id=item.id)
        dst_follower_id=user_follow.objects.filter(dst_user_id=item.id)
        ban_id=user_ban.objects.filter(src_user_id=item.id)
        src_follower=[]
        dst_follower=[]
        banner=[]
        for src_item in src_follower_id:
            src_follower.append({
                "id":src_item.dst_user_id,
                "user_name":user.objects.get(id= src_item.dst_user_id).user_name,
                "user_image_name":user.objects.get(id= src_item.dst_user_id).user_image_name.name
            })
        for dst_item in dst_follower_id:
            dst_follower.append({
                "id":dst_item.src_user_id,
                "user_name":user.objects.get(id= dst_item.src_user_id).user_name,
                "user_image_name":user.objects.get(id= dst_item.src_user_id).user_image_name.name
            })
        for ban_item in ban_id:
            banner.append({
                "id":ban_item.dst_user_id,
                "user_name":user.objects.get(id= ban_item.dst_user_id).user_name,
                "user_image_name":user.objects.get(id= ban_item.dst_user_id).user_image_name.name
            })
        res={
            "id":item.id,
            "user_name":item.user_name,
            "password":item.password,
            "email":item.email,
            "user_image_name":item.user_image_name.name,
            "introduction":item.introduction,
            "account_birth":item.account_birth,
            "src_follower_id":src_follower,
            "dst_follower_id":dst_follower,
            "ban_id":banner
        }
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":res})
def user_register(request):
    json_param=json.loads( request.body.decode())
    try:
        user.objects.get(user_name=json_param['user_name'])
    except:
        try:
            user.objects.get(email=json_param['email'])
        except:
            new_user=user()
            new_user.user_name=json_param['user_name']
            new_user.password=json_param['password']
            new_user.email=json_param['email']
            new_user.save()
            return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
        else:
            return JsonResponse(status=400,data={"status":"BS.400","msg":"email already exists"})
    else:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"user_name already exists"})
def user_update(request):
    try:
        user_item=user.objects.get(id=request.POST.get('user_id'))
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"user not found"})
    else:
        if request.POST.get('password')!='':

            user_item.password=request.POST.get('password')
        elif request.POST.get('introduction')!='':

            user_item.introduction=request.POST.get('introduction')
        elif request.FILES.get('user_image')!=None:
            user_item.user_image_name=request.FILES.get('user_image')
        elif request.POST.get('email')!='':

            user_item.email=request.POST.get('email')
        elif request.POST.get('user_name')!='':
            try:
                user_item1=user.objects.get(user_name=request.POST.get('user_name'))
            except:
                user_item.user_name=request.POST.get('user_name')
                
            else:
                return JsonResponse(status=400,data={"status":"BS.400","msg":"user_name already exists"})
        user_item.save()
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def follow(request):
    json_param=json.loads( request.body.decode())
    try:
        user_item=user.objects.get(id=json_param['src_user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"src user not found"})
    else:
        try:
            user_item=user.objects.get(id=json_param['dst_user_id'])
        except:
            return JsonResponse(status=400,data={"status":"BS.400","msg":"dst user not found"})
        else:
            new_follow=user_follow()
            new_follow.src_user_id=json_param['src_user_id']
            new_follow.dst_user_id=json_param['dst_user_id']
            new_follow.save()
            return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def delete_follow(request):
    json_param=json.loads( request.body.decode())
    try:
        follow_item=user_follow.objects.get(src_user_id=json_param['src_user_id'],dst_user_id=json_param['dst_user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"follow not found"})
    else:
        follow_item.delete()
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def ban(request):
    json_param=json.loads( request.body.decode())
    try:
        user_item=user.objects.get(id=json_param['src_user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"src user not found"})
    else:
        try:
            user_item=user.objects.get(id=json_param['dst_user_id'])
        except:
            return JsonResponse(status=400,data={"status":"BS.400","msg":"dst user not found"})
        else:
            follow_item=user_follow.objects.filter(src_user_id=json_param['src_user_id'],dst_user_id=json_param['dst_user_id'])
            for i in follow_item:
                i.delete()
            new_follow=user_ban()
            new_follow.src_user_id=json_param['src_user_id']
            new_follow.dst_user_id=json_param['dst_user_id']
            new_follow.save()
            return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def delete_ban(request):
    json_param=json.loads( request.body.decode())
    try:
        follow_item=user_ban.objects.get(src_user_id=json_param['src_user_id'],dst_user_id=json_param['dst_user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"follow not found"})
    else:
        follow_item.delete()
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def item_send(request):
    try:
        user_item=user.objects.get(id=request.POST.get('user_id'))
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"user not found"})
    else:
        new_item=item()
        new_item.type=request.POST.get('type')
        new_item.user_id=request.POST.get('user_id')
        new_item.location=request.POST.get('location')
        new_item.title=request.POST.get('title')
        new_item.content=request.POST.get('content')
        new_item.file=request.FILES.get('file')
        if int(new_item.type)==3:
            new_item.fake_image=request.FILES.get('fake_image')
            
        new_item.save()
        followers=user_follow.objects.filter(dst_user_id=request.POST.get('user_id'))
        for follower in followers:
            follower_item=user.objects.get(id=follower.src_user_id)
            new_notice=user_notice()
            new_notice.src_user_id=user_item.id
            new_notice.dst_user_id=follower_item.id
            new_notice.content="您关注的"+user_item.user_name+"发布了一条动态"
            new_notice.type="item"
            new_notice.item_id=new_item.id
            new_notice.save()
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})

def item_delete(request):
    json_param=json.loads( request.body.decode())
    try:
        item_item=item.objects.get(id=json_param['item_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"item not found"})
    else:
        if item_item.user_id!=json_param['user_id']:
            return JsonResponse(status=400,data={"status":"BS.400","msg":"not right user"})
        else:
            
            notices=user_notice.objects.filter(item_id=item_item.id,type="item")
            for notice in notices:
                notice.delete()
            item_item.delete()
            return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def like(request):
    json_param=json.loads( request.body.decode())
    try:
        item_item=item.objects.get(id=json_param['item_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"item not found"})
    try:
        user_item=user.objects.get(id=json_param['user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"user not found"})
    new_like=user_like()
    new_like.item_id=json_param['item_id']
    new_like.user_id=json_param['user_id']
    new_like.save()
    item_item.like_account+=1
    item_item.save()
    new_notice=user_notice()
    new_notice.src_user_id=user_item.id
    new_notice.dst_user_id=item_item.user_id
    new_notice.content=user_item.user_name+"给您的动态点赞了"
    new_notice.type="like"
    new_notice.item_id=new_like.id
    new_notice.save()
    return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def delete_like(request):
    json_param=json.loads( request.body.decode())
    print(json_param)
    try:
        like_item=user_like.objects.get(item_id=json_param['item_id'],user_id=json_param['user_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"like not found"})
    else:
        item_item=item.objects.get(id=json_param['item_id'])
        item_item.like_account-=1
        item_item.save()
        notice=user_notice.objects.get(item_id=like_item.id,type="like")
        notice.delete()
        like_item.delete()
        return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def get_item(request):
    json_param=json.loads( request.body.decode())
    items=item.objects.all()
    followers=user_follow.objects.filter(src_user_id=json_param['user_id'])
    banners=user_ban.objects.filter(src_user_id=json_param['user_id'])
    followers_id=[]
    banners_id=[]
    to_upload=[]
    for follower in followers:
        followers_id.append(follower.dst_user_id)
    for banner in banners:
        banners_id.append(banner.dst_user_id)
    for item_item in items:
        if json_param['type'][item_item.type]==False:
            continue
        if json_param['follower']==True:
            if item_item.user_id not in followers_id:
                continue
        if json_param['search_type']=="title":
            if json_param['search']!='':
                if fuzz.partial_ratio(json_param['search'], item_item.title)<=50:
                    continue
        elif json_param['search_type']=="user":
            if json_param['search']!='':
                if fuzz.partial_ratio(json_param['search'], user.objects.get(id=item_item.user_id).user_name)<=50:
                    continue
        elif json_param['search_type']=="content":
            if json_param['search']!='':
                if fuzz.partial_ratio(json_param['search'], item_item.content)<=50:
                    continue
        if item_item.user_id in banners_id:
            continue
        

        data_json={}
        data_json['item_id']=item_item.id
        data_json['user_id']=item_item.user_id

        if item_item.user_id in followers_id:
            data_json['is_followed']=True
        else:
            data_json['is_followed']=False
        data_json['user_name']=user.objects.get(id=item_item.user_id).user_name
        data_json['content']=item_item.content
        data_json['title']=item_item.title
        data_json['location']=item_item.location
        data_json['type']=item_item.type
        data_json['user_image_name']=user.objects.get(id=item_item.user_id).user_image_name.name
        data_json['created_time']=item_item.created_time
        data_json['comment_count']=item_item.comment_account
        data_json['file_name']=item_item.file.name
        data_json['like_count']=item_item.like_account
        try:
            user_like.objects.get(user_id=json_param['user_id'],item_id=item_item.id)
        except:
            data_json['is_liked']=False
        else:
            data_json['is_liked']=True
        like_users=user_like.objects.filter(item_id=item_item.id)
        data_json['like_user']=[]
        for like_user in like_users:
            data_json['like_user'].append({
                "user_id":like_user.user_id,
                "user_name":user.objects.get(id= like_user.user_id).user_name
            })
        if int(item_item.type)==3:
            data_json['fake_image']=item_item.fake_image.name

        to_upload.append(data_json)
    if json_param['sort']=="time":
        to_upload=sorted(to_upload,key=lambda x:x['created_time'])
    elif json_param['sort']=="like":
        to_upload=sorted(to_upload,key=lambda x:x['like_count'])
    elif json_param['sort']=="comment":
        to_upload=sorted(to_upload,key=lambda x:x['comment_count'])
    if len(to_upload)==0:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"no item"})
    to_upload.reverse()
    return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":to_upload})

def get_self_item(request):
    json_param=json.loads( request.body.decode())
    items=item.objects.filter(user_id=int(json_param['user_id']))
    res=[]
    for item_item in items:
        data_json={}
        data_json['item_id']=item_item.id
        data_json['user_id']=item_item.user_id
        data_json['content']=item_item.content
        data_json['title']=item_item.title
        try:
            user_like.objects.get(user_id=json_param['user_id'],item_id=item_item.id)
        except:
            data_json['is_liked']=False
        else:
            data_json['is_liked']=True
        data_json['location']=item_item.location
        data_json['user_name']=user.objects.get(id=item_item.user_id).user_name
        data_json['type']=item_item.type
        data_json['created_time']=item_item.created_time
        data_json['comment_count']=item_item.comment_account
        data_json['file_name']=item_item.file.name
        data_json['like_count']=item_item.like_account
        like_users=user_like.objects.filter(item_id=item_item.id)
        data_json['like_user']=[]
        for like_user in like_users:
            data_json['like_user'].append({
                "user_id":like_user.user_id,
                "user_name":user.objects.get(id= like_user.user_id).user_name
            })
        if int(item_item.type)==3:
            data_json['fake_image']=item_item.fake_image.name
        res.append(data_json)
    return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":res})
def get_comment(request):
    json_param=json.loads( request.body.decode())
    comments=user_comment.objects.filter(item_id=json_param['id'])
    res=[]
    item_item=item.objects.get(id=json_param['id'])
    data_json={}
    data_json['item_id']=item_item.id
    data_json['user_id']=item_item.user_id
    data_json['user_image_name']=user.objects.get(id=item_item.user_id).user_image_name.name
    data_json['content']=item_item.content
    data_json['title']=item_item.title
    data_json['location']=item_item.location
    data_json['user_name']=user.objects.get(id=item_item.user_id).user_name
    data_json['type']=item_item.type
    data_json['created_time']=item_item.created_time
    data_json['comment_count']=item_item.comment_account
    data_json['file_name']=item_item.file.name
    data_json['like_count']=item_item.like_account
    data_json['fake_image']=item_item.fake_image.name
    like_users=user_like.objects.filter(item_id=item_item.id)
    data_json['like_user']=[]
    for like_user in like_users:
        data_json['like_user'].append({
            "user_id":like_user.user_id,
            "user_name":user.objects.get(id= like_user.user_id).user_name
        })
    for comment in comments:
        res.append({
            "id":comment.id,
            "item_id":comment.item_id,
            "user_id":comment.user_id,
            "content":comment.content,
            "created_time":comment.created_time
        })
    data_json['comments']=res
    return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":data_json})
def comment_delete(request):
    json_param=json.loads( request.body.decode())
    try:
        comment_item=user_comment.objects.get(id=json_param['comment_id'])
    except:
        return JsonResponse(status=400,data={"status":"BS.400","msg":"item not found"})
    else:
        if comment_item.user_id!=json_param['user_id']:
            return JsonResponse(status=400,data={"status":"BS.400","msg":"not right user"})
        else:
            item_item=item.objects.get(id=json_param['item_id'])
            item_item.comment_account-=1
            item_item.save()
            comment_item.delete()
            notice=user_notice.objects.get(item_id=json_param['comment_id'],type="comment")
            notice.delete()
            return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})
def get_notice(request):
    json_param=json.loads( request.body.decode())
    
    notices=user_notice.objects.filter(dst_user_id=json_param['user_id'])
    res=[]
    for notice in notices:
        res.append(notice.content)
    print(res)
    return JsonResponse(status=200,data={"status":"BS.200","msg":"ok","data":res})
def comment_send(request):
    json_param=json.loads( request.body.decode())
    comment_item=user_comment()
    comment_item.user_id=json_param['user_id']
    comment_item.content=json_param['content']
    comment_item.item_id=json_param['item_id']
    comment_item.save()
    new_notice=user_notice()
    new_notice.src_user_id=json_param['user_id']
    new_notice.dst_user_id=item.objects.get(id=json_param['item_id']).user_id
    new_notice.content=user.objects.get(id=json_param['user_id']).user_name+"给您的动态评论了"
    new_notice.type="comment"
    new_notice.item_id=comment_item.id
    new_notice.save()
    item_item=item.objects.get(id=json_param['item_id'])
    item_item.comment_account+=1
    item_item.save()
    return JsonResponse(status=200,data={"status":"BS.200","msg":"ok"})