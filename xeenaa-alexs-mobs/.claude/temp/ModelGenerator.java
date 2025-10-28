import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Programmatic GeckoLib model generator for Fly entity (no external dependencies).
 *
 * This tool generates .geo.json files programmatically without needing Blockbench.
 * Part of TASK-004 COMPARISON TEST (Approach A).
 *
 * @author implementation-agent
 * @version 1.0
 * @since 2025-10-26
 */
public class ModelGenerator {

    /**
     * Main entry point - generates Fly model.
     */
    public static void main(String[] args) {
        try {
            generateFlyModel();
            System.out.println("✅ Fly model generated successfully!");
        } catch (IOException e) {
            System.err.println("❌ Failed to generate model: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates the Fly mob model in GeckoLib format.
     *
     * Model structure:
     * - root (pivot point)
     *   - body (main body cube: 4x3x3)
     *     - head (head cube: 3x3x3)
     *     - wing_left (thin wing: 6x4x1)
     *     - wing_right (thin wing: 6x4x1)
     *     - leg_front_left (thin leg: 1x3x1)
     *     - leg_front_right (thin leg: 1x3x1)
     *     - leg_middle_left (thin leg: 1x3x1)
     *     - leg_middle_right (thin leg: 1x3x1)
     *     - leg_back_left (thin leg: 1x3x1)
     *     - leg_back_right (thin leg: 1x3x1)
     */
    private static void generateFlyModel() throws IOException {
        String outputPath = "fly_programmatic.geo.json";
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            writer.println("{");
            writer.println("  \"format_version\": \"1.12.0\",");
            writer.println("  \"minecraft:geometry\": [");
            writer.println("    {");
            writer.println("      \"description\": {");
            writer.println("        \"identifier\": \"geometry.fly\",");
            writer.println("        \"texture_width\": 32,");
            writer.println("        \"texture_height\": 32,");
            writer.println("        \"visible_bounds_width\": 2,");
            writer.println("        \"visible_bounds_height\": 1,");
            writer.println("        \"visible_bounds_offset\": [0, 0.25, 0]");
            writer.println("      },");
            writer.println("      \"bones\": [");

            // Root bone
            writer.println("        {");
            writer.println("          \"name\": \"root\",");
            writer.println("          \"pivot\": [0, 0, 0]");
            writer.println("        },");

            // Body bone
            writer.println("        {");
            writer.println("          \"name\": \"body\",");
            writer.println("          \"parent\": \"root\",");
            writer.println("          \"pivot\": [0, 4.5, 0],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [-2, 3, -1.5],");
            writer.println("              \"size\": [4, 3, 3],");
            writer.println("              \"uv\": [0, 0]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Head bone
            writer.println("        {");
            writer.println("          \"name\": \"head\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [0, 6, -1.5],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [-1.5, 6, -3],");
            writer.println("              \"size\": [3, 3, 3],");
            writer.println("              \"uv\": [12, 0]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Left wing
            writer.println("        {");
            writer.println("          \"name\": \"wing_left\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [2, 5, 0],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [2, 4, -2],");
            writer.println("              \"size\": [6, 4, 1],");
            writer.println("              \"uv\": [0, 10]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Right wing
            writer.println("        {");
            writer.println("          \"name\": \"wing_right\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [-2, 5, 0],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [-8, 4, -2],");
            writer.println("              \"size\": [6, 4, 1],");
            writer.println("              \"uv\": [0, 10]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Front left leg
            writer.println("        {");
            writer.println("          \"name\": \"leg_front_left\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [1.5, 3, -1],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [1, 0, -1.5],");
            writer.println("              \"size\": [1, 3, 1],");
            writer.println("              \"uv\": [0, 6]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Front right leg
            writer.println("        {");
            writer.println("          \"name\": \"leg_front_right\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [-1.5, 3, -1],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [-2, 0, -1.5],");
            writer.println("              \"size\": [1, 3, 1],");
            writer.println("              \"uv\": [0, 6]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Middle left leg
            writer.println("        {");
            writer.println("          \"name\": \"leg_middle_left\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [1.5, 3, 0],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [1, 0, -0.5],");
            writer.println("              \"size\": [1, 3, 1],");
            writer.println("              \"uv\": [4, 6]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Middle right leg
            writer.println("        {");
            writer.println("          \"name\": \"leg_middle_right\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [-1.5, 3, 0],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [-2, 0, -0.5],");
            writer.println("              \"size\": [1, 3, 1],");
            writer.println("              \"uv\": [4, 6]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Back left leg
            writer.println("        {");
            writer.println("          \"name\": \"leg_back_left\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [1.5, 3, 1],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [1, 0, 0.5],");
            writer.println("              \"size\": [1, 3, 1],");
            writer.println("              \"uv\": [8, 6]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        },");

            // Back right leg (last bone, no trailing comma)
            writer.println("        {");
            writer.println("          \"name\": \"leg_back_right\",");
            writer.println("          \"parent\": \"body\",");
            writer.println("          \"pivot\": [-1.5, 3, 1],");
            writer.println("          \"cubes\": [");
            writer.println("            {");
            writer.println("              \"origin\": [-2, 0, 0.5],");
            writer.println("              \"size\": [1, 3, 1],");
            writer.println("              \"uv\": [8, 6]");
            writer.println("            }");
            writer.println("          ]");
            writer.println("        }");

            writer.println("      ]");
            writer.println("    }");
            writer.println("  ]");
            writer.println("}");
        }

        System.out.println("Generated: " + outputPath);
        System.out.println("Bones: 11 (root + body + head + 2 wings + 6 legs)");
        System.out.println("Model: Fly (tiny insect with 6 legs and 2 wings)");
    }
}
