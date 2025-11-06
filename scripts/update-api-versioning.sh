#!/bin/bash
# Script to update all REST controllers to use /api/v1 versioning

set -e

echo "=========================================="
echo "Updating API Versioning for All Controllers"
echo "=========================================="
echo ""

# List of controllers to update with their old and new paths
declare -A CONTROLLERS=(
    ["src/main/java/com/pragma/vc/tracker/projectmanagement/infrastructure/web/ProjectController.java"]="/api/projects:/api/v1/projects"
    ["src/main/java/com/pragma/vc/tracker/peoplemanagement/infrastructure/web/ChapterController.java"]="/api/chapters:/api/v1/chapters"
    ["src/main/java/com/pragma/vc/tracker/peoplemanagement/infrastructure/web/PragmaticController.java"]="/api/pragmatics:/api/v1/pragmatics"
    ["src/main/java/com/pragma/vc/tracker/knowledgecatalog/infrastructure/web/CategoryController.java"]="/api/categories:/api/v1/categories"
    ["src/main/java/com/pragma/vc/tracker/knowledgecatalog/infrastructure/web/LevelController.java"]="/api/levels:/api/v1/levels"
    ["src/main/java/com/pragma/vc/tracker/knowledgeapplication/infrastructure/web/AppliedKnowledgeController.java"]="/api/applied-knowledge:/api/v1/applied-knowledge"
    ["src/main/java/com/pragma/vc/tracker/projectmanagement/infrastructure/adapter/web/TerritoryController.java"]="/api/territories:/api/v1/territories"
)

for file in "${!CONTROLLERS[@]}"; do
    if [ -f "$file" ]; then
        mapping="${CONTROLLERS[$file]}"
        old_path="${mapping%%:*}"
        new_path="${mapping##*:}"

        echo "Updating: $file"
        echo "  $old_path → $new_path"

        # Update the RequestMapping path
        sed -i "s|@RequestMapping(\"$old_path\")|@RequestMapping(\"$new_path\")|g" "$file"

        echo "  ✓ Updated"
    else
        echo "⚠ File not found: $file"
    fi
    echo ""
done

echo "=========================================="
echo "API Versioning Update Complete!"
echo "=========================================="
echo ""
echo "All controllers now use /api/v1 versioning"
echo ""
echo "Updated controllers:"
for file in "${!CONTROLLERS[@]}"; do
    if [ -f "$file" ]; then
        mapping="${CONTROLLERS[$file]}"
        new_path="${mapping##*:}"
        echo "  - $new_path"
    fi
done
