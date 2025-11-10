# Kibana Dashboard Templates

This directory contains pre-configured Kibana dashboards for the Knowledge Tracker system.

## Quick Import

### Option 1: Import Pre-built Dashboard (Fastest)

1. **Start Kibana**
   ```bash
   chmod +x scripts/start-kibana.sh
   ./scripts/start-kibana.sh
   ```

2. **Access Kibana**
   - Open http://localhost:5601
   - Wait for Kibana to fully load

3. **Import Dashboard**
   - Click hamburger menu (☰) → **Management** → **Stack Management**
   - Click **Saved Objects** (under Kibana section)
   - Click **Import** button (top right)
   - Select file: `kibana/dashboard-template.ndjson`
   - Click **Import**
   - If prompted about conflicts, choose **Overwrite**

4. **View Dashboard**
   - Click hamburger menu → **Analytics** → **Dashboard**
   - Select **"Knowledge Application Report - Users by Project"**

### Option 2: Manual Setup (Full Control)

Follow the complete step-by-step guide:
- See: `docs/kibana-dashboard-setup.md`

---

## What's Included in the Template

The `dashboard-template.ndjson` file contains:

1. **Index Pattern**: `applied-knowledge-*`
   - Configured with all necessary field mappings
   - Time field: `updated_at`

2. **Visualization: "Users Currently Applying Each Knowledge"**
   - Type: Vertical Bar Chart
   - Shows: Unique user count per knowledge type
   - Pre-filtered: Only current assignments (`is_currently_assigned = true`)

3. **Visualization: "Users by Knowledge and Project (Detailed)"**
   - Type: Data Table
   - Shows: Two-level breakdown (Knowledge → Projects → User Count)
   - Sortable and expandable rows

4. **Dashboard: "Knowledge Application Report - Users by Project"**
   - Combines both visualizations
   - Pre-filtered for current assignments
   - Time range: Last 90 days
   - Responsive layout

---

## Customizing After Import

### Add More Filters

After importing, you can add filters to answer specific questions:

**Example: "Users applying Java in Colombia"**
1. Open the dashboard
2. Click **Add filter**
3. Add filter: `knowledge_name.keyword` is `Java`
4. Add filter: `territory_name.keyword` is `Colombia`
5. Click **Save** to update the dashboard

### Add More Visualizations

**Example: Add a Pie Chart for Territory Distribution**
1. Click **Edit** on the dashboard
2. Click **Create visualization**
3. Select **Pie** chart
4. Configure:
   - Metric: Unique Count of `pragmatic_id.keyword`
   - Slice by: `territory_name.keyword`
5. Click **Save and return**

---

## Testing Queries Before Dashboard Creation

Before creating complex dashboards, test your queries in Dev Tools:

1. **Open Dev Tools**
   - http://localhost:5601/app/dev_tools#/console

2. **Run Sample Queries**
   - See all available test queries: `docs/elasticsearch-test-queries.md`
   - Copy/paste queries into Dev Tools console
   - Press `Ctrl+Enter` to execute

3. **Verify Data**
   ```
   GET /applied-knowledge-*/_search
   {
     "size": 5,
     "query": { "match_all": {} }
   }
   ```

---

## Common Use Cases

### Use Case 1: "How many users are applying Java?"
1. Open dashboard
2. Use the search bar: `knowledge_name.keyword: "Java"`
3. View the bar chart for the count

### Use Case 2: "Which projects use Java and how many users per project?"
1. Open dashboard
2. Filter: `knowledge_name.keyword: "Java"`
3. Look at the data table for project breakdown

### Use Case 3: "Users applying Java in Active Projects in Colombia"
1. Open dashboard
2. Add filters:
   - `knowledge_name.keyword: "Java"`
   - `project_status.keyword: "Active"`
   - `territory_name.keyword: "Colombia"`
3. Switch to **Discover** view for employee names
4. Add columns: `pragmatic_full_name`, `pragmatic_email`, `project_name`

---

## Exporting Dashboard Changes

After customizing your dashboard, export it to save your work:

1. **Export Dashboard**
   - Go to **Stack Management** → **Saved Objects**
   - Search for your dashboard
   - Select it (checkbox)
   - Click **Export** (top right)
   - Save the `.ndjson` file

2. **Version Control**
   - Save exported file to `kibana/` directory
   - Commit to git for team sharing

---

## Troubleshooting

### Dashboard Shows No Data

**Check 1: Verify Elasticsearch has data**
```bash
curl "http://localhost:9200/applied-knowledge-*/_count"
```

**Check 2: Check time range**
- Dashboard default: Last 90 days
- Click the time picker (top right)
- Change to "Last 1 year" or wider range

**Check 3: Check filters**
- Review applied filters in the dashboard
- Temporarily disable all filters to see if data appears

### Fields Not Appearing

**Refresh field list:**
1. Go to **Stack Management** → **Index Patterns**
2. Select `applied-knowledge-*`
3. Click the **Refresh** button (circular arrow icon)

### Visualization Errors

**Check field types:**
- Ensure you're using `.keyword` suffix for exact matching
- Example: `knowledge_name.keyword` not `knowledge_name`

---

## Creating Additional Dashboards

### Executive Dashboard (High-Level Metrics)

**Key Metrics:**
- Total unique users currently assigned
- Total active projects
- Knowledge diversity (unique knowledge types in use)
- Top 5 most-used knowledge types

**Visualizations:**
- Metric panels for counts
- Pie chart for knowledge distribution
- Trend line for assignments over time

### Project Manager Dashboard (Project-Focused)

**Key Metrics:**
- Users per project
- Knowledge coverage per project
- Project duration and status

**Visualizations:**
- Table: Projects with user counts and knowledge lists
- Bar chart: Projects by number of assigned users
- Timeline: Project start/end dates

### HR Dashboard (Skills-Focused)

**Key Metrics:**
- User proficiency levels
- Knowledge gaps (underutilized knowledge)
- Training needs

**Visualizations:**
- Heatmap: Users by knowledge and proficiency level
- Table: Users with their knowledge and levels
- Bar chart: Knowledge distribution by proficiency

---

## Advanced: Scheduled Reports

If you have Kibana subscription features (Elastic Stack):

1. **Create PDF Report**
   - Open dashboard
   - Click **Share** → **PDF Reports**
   - Click **Generate PDF**

2. **Schedule Report**
   - Click **Share** → **PDF Reports** → **Advanced options**
   - Set schedule (daily, weekly, monthly)
   - Add email recipients
   - Save

---

## Resources

- **Setup Guide**: `docs/kibana-dashboard-setup.md`
- **Test Queries**: `docs/elasticsearch-test-queries.md`
- **Logstash Pipelines**: `logstash/pipeline/applied-knowledge-pipeline.conf`

---

## Dashboard Maintenance

### Regular Tasks

1. **Refresh index patterns** (monthly)
   - New fields may be added to the index
   - Refresh ensures they appear in Kibana

2. **Review dashboard performance** (as needed)
   - Large aggregations may slow down
   - Consider reducing date ranges or adding filters

3. **Export dashboards** (after changes)
   - Version control your dashboards
   - Share with team members

---

**Last Updated**: 2025-11-07
**Kibana Version**: 8.11.0
**Dashboard Version**: 1.0