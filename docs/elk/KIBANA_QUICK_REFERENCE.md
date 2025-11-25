# Kibana Quick Reference

## URLs

- **Kibana Dashboard:** http://localhost:5601
- **Elasticsearch:** http://localhost:9200
- **PostgreSQL (via pgAdmin):** http://localhost:5050

## Essential Commands

### Sync Data to Elasticsearch
```bash
./scripts/sync-postgres-to-es.sh
```

### Setup Kibana Index Patterns
```bash
./scripts/setup-kibana-dashboard.sh
```

### Check Elasticsearch Indices
```bash
curl "http://localhost:9200/_cat/indices?v"
```

### Check Document Count
```bash
curl "http://localhost:9200/applied-knowledge/_count" | jq
```

---

## Available Indices

| Index | Documents | Description |
|-------|-----------|-------------|
| `accounts` | 8 | Client organizations |
| `projects` | 11 | Client projects |
| `pragmatics` | 21 | Pragma employees |
| `knowledge` | 50 | Technical skills |
| `applied-knowledge` | 50 | **CORE**: Knowledge applications |
| `territories` | 6 | Geographic regions |

---

## Quick Dashboard Ideas

### 1. Executive Summary Dashboard
- Total knowledge applications (Metric)
- Projects by territory (Pie chart)
- Active vs inactive projects (Donut)
- Knowledge by category (Bar chart)

### 2. Skills Analytics Dashboard
- Top 10 most applied knowledge (Horizontal bar)
- Knowledge proficiency distribution (Stacked bar)
- Knowledge application heat map (Heat map)
- Unused knowledge items (Filter: times_applied = 0)

### 3. Resource Management Dashboard
- Pragmatics by KC team (Table)
- Pragmatics by chapter (Bar chart)
- Employee status distribution (Pie chart)
- Knowledge applications per pragmatic (Bar chart)

### 4. Project Insights Dashboard
- Projects timeline (Timeline)
- Projects by account (Table)
- Average project duration (Metric)
- Knowledge applied per project (Bar chart)

---

## Common Kibana Searches

Use these in the Kibana search bar:

```
# Find all Java knowledge applications
knowledge_name: "Java"

# Find active projects
status: "Active"

# Find pragmatics in specific KC team
kc_team_name: "KC Ciencias de la Computación"

# Find expert-level knowledge applications
knowledge_level_name: "Expert"

# Find projects for specific account
account_name: "Bancolombia"

# Find pragmatics in Colombia
territory_name: "Colombia"
```

---

## Visualization Cheat Sheet

### Metric
- **Use for:** Single value KPIs
- **Good fields:** Count, Sum, Average
- **Example:** Total projects, Average team size

### Pie/Donut Chart
- **Use for:** Category distribution
- **Good fields:** status, territory, category
- **Example:** Projects by status, Pragmatics by team

### Bar Chart (Vertical)
- **Use for:** Comparing categories
- **Good fields:** Counts by category
- **Example:** Knowledge by category

### Bar Chart (Horizontal)
- **Use for:** Rankings, Top N lists
- **Good fields:** Top items
- **Example:** Top 10 knowledge skills

### Heat Map
- **Use for:** 2D distributions
- **Good fields:** Two categorical dimensions
- **Example:** Projects × Knowledge matrix

### Data Table
- **Use for:** Detailed listings
- **Good fields:** Multiple dimensions
- **Example:** Employee roster with details

### Line/Area Chart
- **Use for:** Trends over time
- **Good fields:** Date histograms
- **Example:** Knowledge applications per month

---

## Quick Filters

Add these filters to your dashboards:

### By Geography
- Field: `territory_name.keyword`
- Values: Colombia, México, Argentina, Chile, Perú, Brasil

### By Status
- Field: `status.keyword`
- Values: Active, Inactive, Completed, OnLeave

### By Knowledge Category
- Field: `category_name.keyword` or `knowledge_category.keyword`
- Values: Programming Language, Framework, Platform, Tool, Technique

### By Proficiency Level
- Field: `knowledge_level_name.keyword`
- Values: Beginner, Intermediate, Advanced, Expert

---

## Troubleshooting

### Dashboard shows "No results found"

1. Check time range (if using time-based index pattern)
2. Remove all filters
3. Verify data exists:
   ```bash
   curl "http://localhost:9200/INDEX_NAME/_search?size=1" | jq
   ```

### Visualization shows wrong data

1. Check the index pattern selected
2. Verify field mapping:
   - Use `.keyword` for exact matches
   - Without `.keyword` for analyzed text
3. Example: `status.keyword` not `status`

### Need to refresh data

```bash
# Re-sync from PostgreSQL
./scripts/sync-postgres-to-es.sh

# Refresh browser or set auto-refresh in Kibana
```

---

## Best Practices

### Naming Conventions
- Dashboard names: Use descriptive titles
- Visualization names: Include metric and dimension
- Example: "Projects by Territory (Pie)" not just "Pie Chart"

### Color Schemes
- Use consistent colors for status across dashboards
- Green: Active, Approved, Success
- Yellow: Pending, In Progress
- Red: Inactive, Failed
- Grey: Unknown, N/A

### Performance Tips
- Limit table rows to 50-100
- Use time ranges to reduce data volume
- Aggregate data when possible
- Avoid too many visualizations on one dashboard (max 8-10)

### Accessibility
- Add descriptions to dashboards
- Use clear labels on axes
- Include legends for multi-series charts
- Test color schemes for color-blind users

---

## Export & Share

### Export Dashboard
1. Go to **Management** → **Saved Objects**
2. Select **Dashboard**
3. Check your dashboard
4. Click **Export**
5. Save as JSON file

### Import Dashboard
1. Go to **Management** → **Saved Objects**
2. Click **Import**
3. Select JSON file
4. Resolve conflicts if needed
5. Click **Import**

### Share Dashboard Link
1. Open dashboard
2. Click **Share** in top-right
3. Copy **Permalink** or **Snapshot URL**

---

## Keyboard Shortcuts

- **/** - Focus search bar
- **Ctrl+/** - Toggle side menu
- **Ctrl+K** - Open quick search
- **F11** - Full screen mode (browser)

---

## Additional Resources

- Full setup guide: `docs/KIBANA_SETUP.md`
- Sync script: `scripts/sync-postgres-to-es.sh`
- Setup script: `scripts/setup-kibana-dashboard.sh`
