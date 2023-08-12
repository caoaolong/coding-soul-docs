#include "vector.h"

struct entry_t {
    int value;
};

static struct entry_t entries[] = {
    {.value = 1}, {.value = 2}, {.value = 3}, {.value = 4}, {.value = 5}
};

int main(int argc, char *argv[])
{
    // 创建vector
    struct vector *vec = vector_create(sizeof(struct entry_t));
    
    size_t count = sizeof(entries) / sizeof(struct entry_t);
    // 放入元素
    for (size_t i = 0; i < count; i++)
    {
        vector_push(vec, &entries[i]);
    }

    // 正向遍历（推荐）
    struct entry_t *e = NULL;
    while ((e = vector_peek(vec))) {
        printf("entry(value = %d)\n", e->value);
    }

    printf("--- Next ---\n");

    // 反向遍历
    vector_set_peek_pointer_end(vec);
    vector_set_flag(vec, VECTOR_FLAG_PEEK_DECREMENT);
    while ((e = vector_peek(vec))) {
        printf("entry(value = %d)\n", e->value);
    }
    vector_unset_flag(vec, VECTOR_FLAG_PEEK_DECREMENT);

    printf("--- Next ---\n");

    // 正向遍历（最不安全）
    e = vector_data_ptr(vec);
    while (e) {
        if (e->value <= 0)
            break;
        printf("entry(value = %d)\n", e->value);
        e++;
    }

    printf("--- Next ---\n");

    // 删除元素
    vector_set_peek_pointer(vec, 0);
    do {
        e = vector_peek(vec);
        if (!e)
            break;
        printf("pop: entry(value = %d)\tcount = %d\n", e->value, vector_count(vec));
        vector_peek_back(vec);
        vector_peek_pop(vec);
    } while (true);

    // 删除元素
    vector_set_peek_pointer(vec, 0);
    do {
        e = vector_peek(vec);
        if (!e)
            break;
        printf("pop: entry(value = %d)\tcount = %d\n", e->value, vector_count(vec));
        vector_pop_last_peek(vec);
    } while (true);

    // 删除元素
    vector_set_peek_pointer(vec, 0);
    do {
        // 错误用法: vector_peek(vec) + vector_pop(vec);
        if (vector_count(vec) == 0) 
            break;
        e = vector_peek_no_increment(vec);
        printf("pop: entry(value = %d)\tcount = %d\n", e->value, vector_count(vec));
        vector_pop_at(vec, 0);
    } while (true);

    // 删除元素
    do {
        // 错误用法: vector_peek(vec) + vector_pop(vec);
        if (vector_count(vec) == 0) 
            break;
        e = vector_back(vec);
        printf("pop: entry(value = %d)\tcount = %d\n", e->value, vector_count(vec));
        vector_pop(vec);
    } while (true);

    // 释放vector
    vector_free(vec);
    return 0;
}