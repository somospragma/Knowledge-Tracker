# Elasticsearch Test Queries for Knowledge Tracker

This file contains useful Elasticsearch queries to test and validate your data before creating Kibana dashboards.

## Prerequisites

Access Elasticsearch at: http://localhost:9200

You can run these queries using:
- **curl** (command line)
- **Kibana Dev Tools Console** (http://localhost:5601/app/dev_tools#/console)
- **Postman** or similar REST client

---

## 1. Check Index Health

### List all applied-knowledge indices
```bash
curl -X GET "http://localhost:9200/_cat/indices/applied-knowledge-*?v"
```

**Kibana Console**:
```
GET /_cat/indices/applied-knowledge-*?v
```

### Count total documents
```bash
curl -X GET "http://localhost:9200/applied-knowledge-*/_count?pretty"
```

**Expected Output**:
```json
{
  "count": 150,  // Your actual count
  "_shards": { ... }
}
```

---

## 2. View Sample Documents

### Get 5 sample documents
```bash
curl -X GET "http://localhost:9200/applied-knowledge-*/_search?size=5&pretty"
```

**Kibana Console**:
```json
GET /applied-knowledge-*/_search
{
  "size": 5,
  "query": {
    "match_all": {}
  }
}
```

### Get documents with all relevant fields
```json
GET /applied-knowledge-*/_search
{
  "size": 3,
  "_source": [
    "pragmatic_full_name",
    "knowledge_name",
    "project_name",
    "project_status",
    "territory_name",
    "is_currently_assigned",
    "assignment_status"
  ],
  "query": {
    "match_all": {}
  }
}
```

---

## 3. Test Business Query: "Users applying Java in Active Projects in Colombia"

### Query for specific knowledge, status, and territory
```json
POST /applied-knowledge-*/_search
{
  "size": 100,
  "_source": [
    "pragmatic_full_name",
    "pragmatic_email",
    "knowledge_name",
    "project_name",
    "territory_name",
    "knowledge_level_name"
  ],
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "knowledge_name.keyword": "Java"
          }
        },
        {
          "term": {
            "project_status.keyword": "Active"
          }
        },
        {
          "term": {
            "territory_name.keyword": "Colombia"
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  },
  "sort": [
    {
      "pragmatic_full_name.keyword": "asc"
    }
  ]
}
```

---

## 4. Aggregation: Count Users by Knowledge

### Count unique users per knowledge type
```json
POST /applied-knowledge-*/_search
{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  },
  "aggs": {
    "by_knowledge": {
      "terms": {
        "field": "knowledge_name.keyword",
        "size": 20,
        "order": {
          "unique_users": "desc"
        }
      },
      "aggs": {
        "unique_users": {
          "cardinality": {
            "field": "pragmatic_id.keyword"
          }
        }
      }
    }
  }
}
```

**Expected Output**:
```json
{
  "aggregations": {
    "by_knowledge": {
      "buckets": [
        {
          "key": "Java",
          "doc_count": 45,
          "unique_users": {
            "value": 12
          }
        },
        {
          "key": "Python",
          "doc_count": 30,
          "unique_users": {
            "value": 8
          }
        }
      ]
    }
  }
}
```

---

## 5. Aggregation: Users by Knowledge and Project (Main Dashboard Query)

### Two-level aggregation: Knowledge → Projects → User Count
```json
POST /applied-knowledge-*/_search
{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "is_currently_assigned": true
          }
        },
        {
          "term": {
            "project_status.keyword": "Active"
          }
        }
      ]
    }
  },
  "aggs": {
    "by_knowledge": {
      "terms": {
        "field": "knowledge_name.keyword",
        "size": 50,
        "order": {
          "_key": "asc"
        }
      },
      "aggs": {
        "by_project": {
          "terms": {
            "field": "project_name.keyword",
            "size": 100,
            "order": {
              "unique_users": "desc"
            }
          },
          "aggs": {
            "unique_users": {
              "cardinality": {
                "field": "pragmatic_id.keyword"
              }
            }
          }
        },
        "total_users_for_knowledge": {
          "cardinality": {
            "field": "pragmatic_id.keyword"
          }
        }
      }
    }
  }
}
```

**Expected Output**:
```json
{
  "aggregations": {
    "by_knowledge": {
      "buckets": [
        {
          "key": "Java",
          "doc_count": 45,
          "total_users_for_knowledge": {
            "value": 12
          },
          "by_project": {
            "buckets": [
              {
                "key": "E-Commerce Platform",
                "doc_count": 8,
                "unique_users": {
                  "value": 5
                }
              },
              {
                "key": "Banking API",
                "doc_count": 6,
                "unique_users": {
                  "value": 4
                }
              }
            ]
          }
        }
      ]
    }
  }
}
```

---

## 6. Get User Names with Details

### Get actual employee names applying specific knowledge
```json
POST /applied-knowledge-*/_search
{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "knowledge_name.keyword": "Java"
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  },
  "aggs": {
    "unique_pragmatics": {
      "terms": {
        "field": "pragmatic_id.keyword",
        "size": 1000
      },
      "aggs": {
        "user_details": {
          "top_hits": {
            "size": 1,
            "_source": [
              "pragmatic_full_name",
              "pragmatic_email",
              "chapter_name",
              "kc_team_name"
            ]
          }
        },
        "projects": {
          "terms": {
            "field": "project_name.keyword",
            "size": 10
          }
        }
      }
    }
  }
}
```

---

## 7. Multi-Knowledge Search (OR condition)

### Find users applying Java OR Python
```json
POST /applied-knowledge-*/_search
{
  "size": 100,
  "_source": [
    "pragmatic_full_name",
    "knowledge_name",
    "project_name"
  ],
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ],
      "should": [
        {
          "term": {
            "knowledge_name.keyword": "Java"
          }
        },
        {
          "term": {
            "knowledge_name.keyword": "Python"
          }
        }
      ],
      "minimum_should_match": 1
    }
  }
}
```

---

## 8. Filter by Territory

### Get all assignments in Colombia
```json
POST /applied-knowledge-*/_search
{
  "size": 100,
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "territory_name.keyword": "Colombia"
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  },
  "aggs": {
    "projects_in_territory": {
      "terms": {
        "field": "project_name.keyword",
        "size": 50
      },
      "aggs": {
        "user_count": {
          "cardinality": {
            "field": "pragmatic_id.keyword"
          }
        }
      }
    }
  }
}
```

---

## 9. Search by Knowledge Level

### Find expert-level users
```json
POST /applied-knowledge-*/_search
{
  "size": 50,
  "_source": [
    "pragmatic_full_name",
    "knowledge_name",
    "knowledge_level_name",
    "project_name"
  ],
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "knowledge_level_name.keyword": "Expert"
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  }
}
```

---

## 10. Time-based Analysis

### Assignments started in the last 30 days
```json
POST /applied-knowledge-*/_search
{
  "size": 100,
  "query": {
    "bool": {
      "must": [
        {
          "range": {
            "onboard_date": {
              "gte": "now-30d/d"
            }
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  }
}
```

---

## 11. Assignment Duration Analysis

### Find long-running assignments (>180 days)
```json
POST /applied-knowledge-*/_search
{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        {
          "range": {
            "assignment_duration_days": {
              "gte": 180
            }
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  },
  "aggs": {
    "avg_duration": {
      "avg": {
        "field": "assignment_duration_days"
      }
    },
    "max_duration": {
      "max": {
        "field": "assignment_duration_days"
      }
    },
    "by_project": {
      "terms": {
        "field": "project_name.keyword",
        "size": 20,
        "order": {
          "avg_duration": "desc"
        }
      },
      "aggs": {
        "avg_duration": {
          "avg": {
            "field": "assignment_duration_days"
          }
        }
      }
    }
  }
}
```

---

## 12. Knowledge Category Analysis

### Group by category, then knowledge
```json
POST /applied-knowledge-*/_search
{
  "size": 0,
  "query": {
    "term": {
      "is_currently_assigned": true
    }
  },
  "aggs": {
    "by_category": {
      "terms": {
        "field": "knowledge_category_name.keyword",
        "size": 20
      },
      "aggs": {
        "by_knowledge": {
          "terms": {
            "field": "knowledge_name.keyword",
            "size": 50
          },
          "aggs": {
            "user_count": {
              "cardinality": {
                "field": "pragmatic_id.keyword"
              }
            }
          }
        }
      }
    }
  }
}
```

---

## 13. Full-Text Search

### Search for knowledge or projects containing "React"
```json
POST /applied-knowledge-*/_search
{
  "size": 50,
  "query": {
    "bool": {
      "must": [
        {
          "multi_match": {
            "query": "React",
            "fields": [
              "knowledge_name",
              "knowledge_description",
              "project_name"
            ]
          }
        },
        {
          "term": {
            "is_currently_assigned": true
          }
        }
      ]
    }
  }
}
```

---

## 14. Check Field Mappings

### View how fields are indexed
```bash
curl -X GET "http://localhost:9200/applied-knowledge-*/_mapping?pretty"
```

**Kibana Console**:
```
GET /applied-knowledge-*/_mapping
```

---

## 15. Composite Report Query (Ready for API)

### Complete query for "Users by Knowledge by Project" report
```json
POST /applied-knowledge-*/_search
{
  "size": 0,
  "query": {
    "bool": {
      "must": [
        { "term": { "is_currently_assigned": true } },
        { "term": { "project_status.keyword": "Active" } }
      ],
      "filter": [
        {
          "range": {
            "updated_at": {
              "gte": "now-90d"
            }
          }
        }
      ]
    }
  },
  "aggs": {
    "knowledge_breakdown": {
      "terms": {
        "field": "knowledge_name.keyword",
        "size": 100,
        "order": { "total_users": "desc" }
      },
      "aggs": {
        "total_users": {
          "cardinality": {
            "field": "pragmatic_id.keyword"
          }
        },
        "projects": {
          "terms": {
            "field": "project_name.keyword",
            "size": 100,
            "order": { "users_in_project": "desc" }
          },
          "aggs": {
            "users_in_project": {
              "cardinality": {
                "field": "pragmatic_id.keyword"
              }
            },
            "user_names": {
              "terms": {
                "field": "pragmatic_full_name.keyword",
                "size": 100
              },
              "aggs": {
                "details": {
                  "top_hits": {
                    "size": 1,
                    "_source": [
                      "pragmatic_email",
                      "knowledge_level_name",
                      "chapter_name"
                    ]
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
```

This query returns a complete hierarchical breakdown:
- Knowledge → Projects → Users → Details

---

## Usage Tips

### Using curl
```bash
# Save query to file
cat > query.json << 'EOF'
{
  "size": 5,
  "query": { "match_all": {} }
}
EOF

# Execute query
curl -X POST "http://localhost:9200/applied-knowledge-*/_search?pretty" \
  -H 'Content-Type: application/json' \
  -d @query.json
```

### Using Kibana Dev Tools
1. Navigate to http://localhost:5601/app/dev_tools#/console
2. Paste any query (without `POST /url` part if shown in examples)
3. Click the green play button or press `Ctrl+Enter`

### Common Query Parameters
- `size`: Number of documents to return (0 for aggregations only)
- `_source`: Specify which fields to return
- `from`: Pagination offset
- `sort`: Sort results
- `pretty`: Format JSON output (curl only)

---

**Created**: 2025-11-07
**Version**: 1.0
