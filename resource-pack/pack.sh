#!/bin/bash

# Main folder
RESOURCE_PACK_FOLDER="cash-resource-pack"

# Input file
PACK_FORMAT_FILE="pack-format.yml"

# Output folder
BUILD_FOLDER="build"

# Create the output folder if it doesn't exist
mkdir -p "$BUILD_FOLDER"

# Read the pack-format.yml file line by line
while IFS=: read -r key value; do
    # Trim whitespace
    key=$(echo $key | xargs)
    value=$(echo $value | xargs)

    # Create the pack.mcmeta file with the appropriate value
    cat <<EOL > "$RESOURCE_PACK_FOLDER/pack.mcmeta"
{
    "pack": {
        "pack_format": $value,
        "description": "Cash official resource pack"
    }
}
EOL

    # Create a zip archive with the key name and include the folder
    zip -r "${BUILD_FOLDER}/${key}.zip" "$RESOURCE_PACK_FOLDER"

    # Remove the pack.mcmeta file after creating the zip
    rm "$RESOURCE_PACK_FOLDER/pack.mcmeta"
done < "$PACK_FORMAT_FILE"

echo "Archiving completed."
