#include "strdb.h"
#include <unistd.h>

#define TEST_DEL

int main(int argc, char *argv[])
{
    char file[] = "strdb.data";
#ifdef TEST_ADD
    char value[] = "Hello,World!";
    int ret = strdb_add(file, STR_CATEGORY_ENG, 0, value);
    printf("%d\n", ret);
    return 0;
#endif

#ifdef TEST_GET
    char *value = strdb_get(file, 2);
    printf("%s\n", value);
    free(value);
    return 0;
#endif

#ifdef TEST_FILE
    char *vpath = "test.txt";
    int ret = strdb_add_file(file, vpath);
    printf("%d\n", ret);
    return 0;
#endif

#ifdef TEST_DEL
    int ret = strdb_del(file, 0);
    printf("%d\n", ret);
    return 0;
#endif

    // 操作提示
    if (argc < 5)
    {
        printf("Usage: strdb file ADD|SET|GET|DEL encoder value|file\n");
        return 0;
    }

    if (!strcmp(argv[2], "ADD"))
        return strdb_add(argv[1], STR_CATEGORY_BOTH, 0, argv[2]);
    else if (!strcmp(argv[2], "SET"))
    {
        int id = (int)strtol(argv[3], NULL, 10);
        return strdb_set(argv[2], id, argv[3]);
    }
    else if (!strcmp(argv[2], "GET"))
    {
        int id = (int)strtol(argv[3], NULL, 10);
        printf("%s\n", strdb_get(argv[2], id));
        return 0;
    }
    else if (!strcmp(argv[2], "DEL"))
    {
        int id = (int)strtol(argv[3], NULL, 10);
        return strdb_del(argv[2], id);
    }

    printf("Error!\n");
    return 0;
}

// 创建字符串项
str_entry_t* strdb_entry_new(uchar encoder, uchar category, uchar state, uint length, char *value)
{
    str_entry_t *entry = malloc(sizeof(str_entry_t));
    memset(entry, 0, sizeof(str_entry_t));
    entry->magic = STR_MAGIC;
    entry->encoder = encoder;
    entry->category = category;
    entry->crypted = state & 0b001;
    entry->referenced = (state & 0b010) >> 1;
    entry->deleted = (state & 0b100) >> 2;
    entry->length = length;
    return entry;
}

// 销毁字符串项
void strdb_entry_destroy(str_entry_t *entry, str_entry_t *fentry, FILE *file, const char *error)
{
    if (entry != NULL)
        free(entry);
    if (fentry != NULL)
        free(fentry);
    if (!file)
        fclose(file);
    if (error)
        perror(error);
}

static int strdb_write(str_entry_t *entry, long size, FILE *file, char *value)
{
    str_entry_t *fentry = malloc(sizeof(str_entry_t));
    int index = 0;
    long i = 0;
    while (i < size)
    {
        fseek(file, i, SEEK_SET);
        memset(fentry, 0, sizeof(str_entry_t));
        if (fread(fentry, sizeof(str_entry_t), 1, file) != 1)
        {
            strdb_entry_destroy(entry, fentry, file, "fread error");
            return STR_ERROR;
        }
        if (!fentry)
            continue;
        if (fentry->magic == STR_MAGIC)
        {
            i += fentry->length + sizeof(str_entry_t);
            index ++;
        } else {
            strdb_entry_destroy(entry, fentry, file, "string entry read error");
            return STR_ERROR;
        }
    }
    entry->id = index;
    fseek(file, i, SEEK_SET);
    if (fwrite(entry, sizeof(str_entry_t), 1, file) != 1 
        || fwrite(value, strlen(value), 1, file) != 1)
    {
        strdb_entry_destroy(entry, fentry, file, "fwrite error");
        return STR_ERROR;
    }
    strdb_entry_destroy(entry, fentry, file, NULL);
    return STR_OK;
}

// 添加字符串
int strdb_add(char *path, char category, char state, char *value)
{
    FILE *file = fopen(path, "a+");

    size_t length = strlen(value);
    str_entry_t *entry = strdb_entry_new(STR_ENCODER_GBK, category, state, length, value);

    fseek(file, 0, SEEK_END);
    long size = ftell(file);
    
    return strdb_write(entry, size, file, value);
}

// 从文件添加字符串
int strdb_add_file(char *path, char *vpath)
{
    FILE *file = fopen(path, "a+");
    if (!file) 
        return STR_ERROR;

    FILE *vfile = fopen(vpath, "r");
    if (!vfile)
    {
        fprintf(stderr, "value fread error\n");
        return STR_ERROR;
    }
    fseek(vfile, 0, SEEK_END);
    long vsize = ftell(vfile);
    char *value = malloc(vsize + 1);
    fseek(vfile, 0, SEEK_SET);
    if (fread(value, vsize, 1, vfile) != 1)
    {
        free(value);
        fclose(vfile);
        perror("fread error");
        return STR_ERROR;
    }
    str_entry_t *entry = strdb_entry_new(STR_ENCODER_UTF8, STR_CATEGORY_BOTH, 0, vsize, value);
    fseek(file, 0, SEEK_END);
    long size = ftell(file);
    return strdb_write(entry, size, file, value);
}

// 获取字符串
char* strdb_get(char *path, int id)
{
    char *rvalue;

    FILE *file = fopen(path, "r");
    if (!file) 
        return NULL;

    str_entry_t *fentry = malloc(sizeof(str_entry_t));
    fseek(file, 0, SEEK_END);
    long size = ftell(file);
    long i = 0;
    while (i < size)
    {
        fseek(file, i, SEEK_SET);
        memset(fentry, 0, sizeof(str_entry_t));
        if (fread(fentry, sizeof(str_entry_t), 1, file) != 1)
        {
            strdb_entry_destroy(NULL, fentry, file, "fread error");
            return NULL;
        }
        i += sizeof(str_entry_t);
        if (fentry->id != id)
        {
            i += fentry->length;
            continue;
        }
        fseek(file, i, SEEK_SET);
        rvalue = malloc(fentry->length + 1);
        if (fread(rvalue, fentry->length, 1, file) != 1)
        {
            free(rvalue);
            strdb_entry_destroy(NULL, fentry, file, "fread error");
            return NULL;
        }
    }
    return rvalue;
}

// 删除字符串
int strdb_del(char *path, int id)
{
    FILE *file = fopen(path, "r+");
    if (!file)
        return STR_ERROR;
    
    fseek(file, 0, SEEK_END);
    long size = ftell(file);
    long i = 0;
    str_entry_t *fentry = malloc(sizeof(str_entry_t));
    while (i < size)
    {
        fseek(file, i, SEEK_SET);
        if (fread(fentry, sizeof(str_entry_t), 1, file) != 1)
        {
            strdb_entry_destroy(NULL, fentry, file, "fread error");
            return STR_ERROR;
        }
        if (fentry->id != id)
            continue;
        fseek(file, i, SEEK_SET);
        long esize = fentry->length + sizeof(str_entry_t);
        char *value = malloc(esize);
        memset(value, 0, esize);
        if (fwrite(value, esize, 1, file) != 1)
        {
            free(value);
            strdb_entry_destroy(NULL, fentry, file, "fread error");
            return STR_ERROR;
        }
        return STR_OK;
    }
    return STR_ERROR;
}

// 修改字符串
int strdb_set(char *file, int id, char *value)
{
    return 0;
}