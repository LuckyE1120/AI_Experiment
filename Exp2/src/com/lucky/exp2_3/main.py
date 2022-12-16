import time

def move(n,A,B):
    print(f'将第{n}个圆盘从',A,'移到',B)

def hanoi(n,A,B,C):
    if n == 1:
        move(1,A,C)
    else:
        hanoi(n - 1, A , C , B)
        move(n , A , C)
        hanoi(n - 1 , B , A , C)

hanoi(3,'A','B','C')


# def move(n, a, b, c):
#     if n == 1:
#         print(f'圆盘{n}:',a, '->', c)
#     else:
#         move(n - 1, a, c, b)
#         print(f'圆盘{n}:',a, '->', c)
#         move(n - 1, b, a, c)
#
# start =time.perf_counter()
# move(7,'x1','x2','x3')
# end = time.perf_counter()
# print('Running time: %s Seconds'%(end-start))

