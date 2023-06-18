#ifndef STRDB_H
#define STRDB_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef unsigned char uchar;
typedef unsigned int uint;

#define STR_OK                  0
#define STR_ERROR               1

#define STR_MAGIC               (0xCA)

#define STR_ENCODER_UTF8        0b00
#define STR_ENCODER_UTF16       0b01
#define STR_ENCODER_GBK         0b10
#define STR_ENCODER_ASCII       0b11

#define STR_CATEGORY_CHN        0b01
#define STR_CATEGORY_ENG        0b10
#define STR_CATEGORY_BOTH       0b11

#define STR_STATE_CRYPT         0b001
#define STR_STATE_REF           0b010
#define STR_STATE_DEL           0b100

typedef struct {
    uchar magic;
    uchar id;
    uchar encoder:2;
    uchar category:2;
    uchar crypted:1;
    uchar referenced:1;
    uchar deleted:1;
    // 保留
    uchar:1;
    uchar order;
    uint length:16;
    // 保留
    uint:16;
} __attribute__((packed)) str_entry_t;

// 创建字符串项
str_entry_t* strdb_entry_new(uchar encoder, uchar category, uchar state, uint length, char *value);

// 销毁字符串项
void strdb_entry_destroy(str_entry_t *entry, str_entry_t *fentry, FILE *file, const char *error);

// 添加字符串
int strdb_add(char *file, char category, char state, char *value);

// 从文件添加字符串
int strdb_add_file(char *file, char *vfile);

// 获取字符串
char* strdb_get(char *file, int id);

// 删除字符串
int strdb_del(char *file, int id);

// 修改字符串
int strdb_set(char *file, int id, char *value);

#endif