#!/usr/bin/env python3
"""
Event2AI MCP Server
Provides tools to process Miro boards and access usecase information
"""

import asyncio
import json
import os
import subprocess
import sys
from pathlib import Path
from typing import Any

import mcp.types as types
from mcp.server import Server
from mcp.server.stdio import stdio_server

# Configuration
PROJECT_ROOT = Path(__file__).parent
TOAI_JSON_DIR = PROJECT_ROOT / "ToAIJsonFile"
MIRO_JSON_PATH = PROJECT_ROOT / "miro" / "clean_dump.json"

# Initialize MCP server
app = Server("event2ai-mcp")


@app.list_tools()
async def list_tools() -> list[types.Tool]:
    """List available tools."""
    return [
        types.Tool(
            name="process_miro_board",
            description="Process Miro board and generate usecase JSON files. This will fetch data from Miro and create/update usecase files in ToAIJsonFile directory.",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        types.Tool(
            name="list_usecases",
            description="List all available usecase files in the ToAIJsonFile directory. This tool automatically syncs with Miro board first (by running Integration.java) to ensure the latest data, then returns a list of usecase names and their file paths.",
            inputSchema={
                "type": "object",
                "properties": {},
                "required": []
            }
        ),
        types.Tool(
            name="get_usecase",
            description="Get the complete content of a specific usecase JSON file. Returns the full usecase definition including aggregates, domain events, entities, and value objects.",
            inputSchema={
                "type": "object",
                "properties": {
                    "usecase_name": {
                        "type": "string",
                        "description": "The name of the usecase (without .json extension)"
                    }
                },
                "required": ["usecase_name"]
            }
        ),
        # types.Tool(
        #     name="get_all_usecases",
        #     description="Get all usecase contents at once. Returns a map of usecase names to their complete JSON content.",
        #     inputSchema={
        #         "type": "object",
        #         "properties": {},
        #         "required": []
        #     }
        # ),
    ]


@app.call_tool()
async def call_tool(name: str, arguments: Any) -> list[types.TextContent]:
    """Handle tool calls."""
    try:
        if name == "process_miro_board":
            result = await process_miro_board()
        elif name == "list_usecases":
            result = await list_usecases()
        elif name == "get_usecase":
            usecase_name = arguments.get("usecase_name")
            if not usecase_name:
                raise ValueError("usecase_name is required")
            result = await get_usecase(usecase_name)
        # elif name == "get_all_usecases":
        #     result = await get_all_usecases()
        else:
            raise ValueError(f"Unknown tool: {name}")

        return [types.TextContent(type="text", text=result)]

    except Exception as e:
        error_msg = f"Error: {str(e)}"
        return [types.TextContent(type="text", text=error_msg)]


async def process_miro_board() -> str:
    """Process Miro board by calling Java Integration class."""
    try:
        # Build classpath with all dependencies from local Maven repository
        drivers_jar = PROJECT_ROOT / "Event2AI-Drivers" / "target" / "Event2AI-Drivers-0.1.0.jar"
        m2_repo = Path.home() / ".m2" / "repository"

        # List of all dependency JARs
        classpath_parts = [
            str(drivers_jar),
            str(m2_repo / "Event2AI" / "Event2AI-Adapter" / "0.1.0" / "Event2AI-Adapter-0.1.0.jar"),
            str(m2_repo / "Event2AI" / "Event2AI-UseCase" / "0.1.0" / "Event2AI-UseCase-0.1.0.jar"),
            str(m2_repo / "Event2AI" / "Event2AI-Entity" / "0.1.0" / "Event2AI-Entity-0.1.0.jar"),
            str(m2_repo / "Event2AI" / "Event2AI-Common" / "0.1.0" / "Event2AI-Common-0.1.0.jar"),
            str(m2_repo / "com" / "google" / "code" / "gson" / "gson" / "2.11.0" / "gson-2.11.0.jar"),
        ]

        # Use ; on Windows, : on Unix-like systems
        sep = ";" if sys.platform == "win32" else ":"
        classpath = sep.join(classpath_parts)

        # Run Java with classpath
        cmd = ["java", "-cp", classpath, "drivers.Integration"]

        process = await asyncio.create_subprocess_exec(
            *cmd,
            stdout=asyncio.subprocess.PIPE,
            stderr=asyncio.subprocess.PIPE,
            cwd=str(PROJECT_ROOT)
        )

        stdout, stderr = await process.communicate()

        if process.returncode != 0:
            error_output = stderr.decode('utf-8', errors='ignore') if stderr else "Unknown error"
            return f"Error processing Miro board:\n{error_output}"

        output = stdout.decode('utf-8', errors='ignore') if stdout else ""

        result = []
        result.append("✓ Miro board processed successfully\n")

        # Include output from Integration class if any
        if output.strip():
            result.append(f"\nOutput:\n{output}\n")

        # Count generated files
        test_dir = TOAI_JSON_DIR / "Test"
        if test_dir.exists():
            json_files = list(test_dir.glob("*.json"))
            result.append(f"\n✓ Generated {len(json_files)} usecase files in ToAIJsonFile/Test/")

        return "".join(result)

    except Exception as e:
        return f"Error executing Java Integration: {str(e)}"


async def list_usecases() -> str:
    """List all usecase files in ToAIJsonFile directory.
    Auto-syncs with Miro board before listing."""
    try:
        # First, sync with Miro board by running Integration.java
        sync_result = await process_miro_board()

        # Check if sync was successful
        if "Error" in sync_result:
            return f"Warning: Sync failed but continuing with existing files.\n{sync_result}\n\n---\n\n"

        # Now list the usecases
        if not TOAI_JSON_DIR.exists():
            return "No usecases found. The ToAIJsonFile directory does not exist."

        usecases = []
        for json_file in TOAI_JSON_DIR.rglob("*.json"):
            relative_path = json_file.relative_to(TOAI_JSON_DIR)
            usecase_name = json_file.stem
            directory = json_file.parent.name

            usecases.append({
                "name": usecase_name,
                "path": str(relative_path),
                "directory": directory
            })

        if not usecases:
            return f"No usecase files found in {TOAI_JSON_DIR}"

        # Group by directory
        by_directory = {}
        for uc in usecases:
            dir_name = uc["directory"]
            if dir_name not in by_directory:
                by_directory[dir_name] = []
            by_directory[dir_name].append(uc)

        result = ["✓ Synced with Miro board\n\n"]
        result.append(f"Found {len(usecases)} usecase(s):\n\n")
        for dir_name, cases in by_directory.items():
            result.append(f"Directory: {dir_name}/\n")
            for uc in cases:
                result.append(f"  - {uc['name']}\n")
                result.append(f"    Path: {uc['path']}\n")
            result.append("\n")

        return "".join(result)

    except Exception as e:
        return f"Error listing usecases: {str(e)}"


async def get_usecase(usecase_name: str) -> str:
    """Get content of a specific usecase file."""
    try:
        # Remove .json extension if provided
        if usecase_name.endswith(".json"):
            usecase_name = usecase_name[:-5]

        if not TOAI_JSON_DIR.exists():
            return "Error: ToAIJsonFile directory does not exist"

        # Search for the file (handle spaces and underscores)
        found_file = None
        for json_file in TOAI_JSON_DIR.rglob("*.json"):
            file_stem = json_file.stem
            # Match exact name or with space/underscore variations
            if (file_stem == usecase_name or
                file_stem.replace(" ", "_") == usecase_name or
                file_stem.replace("_", " ") == usecase_name):
                found_file = json_file
                break

        if not found_file:
            return f"Error: Usecase '{usecase_name}' not found. Use list_usecases to see available usecases."

        # Read and format JSON
        with open(found_file, 'r', encoding='utf-8') as f:
            content = json.load(f)

        pretty_json = json.dumps(content, indent=2, ensure_ascii=False)
        relative_path = found_file.relative_to(TOAI_JSON_DIR)

        result = []
        result.append(f"Usecase: {usecase_name}\n")
        result.append(f"Path: {relative_path}\n\n")
        result.append(pretty_json)

        return "".join(result)

    except Exception as e:
        return f"Error reading usecase: {str(e)}"


# async def get_all_usecases() -> str:
#     """Get all usecase contents."""
#     try:
#         if not TOAI_JSON_DIR.exists():
#             return "Error: ToAIJsonFile directory does not exist"

#         json_files = list(TOAI_JSON_DIR.rglob("*.json"))

#         if not json_files:
#             return f"No usecase files found in {TOAI_JSON_DIR}"

#         result = []
#         result.append(f"Retrieving {len(json_files)} usecase(s):\n")
#         result.append("=" * 80 + "\n\n")

#         for json_file in json_files:
#             usecase_name = json_file.stem
#             relative_path = json_file.relative_to(TOAI_JSON_DIR)

#             with open(json_file, 'r', encoding='utf-8') as f:
#                 content = json.load(f)

#             pretty_json = json.dumps(content, indent=2, ensure_ascii=False)

#             result.append(f"Usecase: {usecase_name}\n")
#             result.append(f"Path: {relative_path}\n")
#             result.append("-" * 80 + "\n")
#             result.append(pretty_json + "\n")
#             result.append("=" * 80 + "\n\n")

#         return "".join(result)

#     except Exception as e:
#         return f"Error reading usecases: {str(e)}"


async def main():
    """Main entry point for the MCP server."""
    # Auto-sync on startup
    print("[Event2AI MCP] Server starting, auto-syncing Miro board...", file=sys.stderr)
    try:
        sync_result = await process_miro_board()
        print("[Event2AI MCP] Initial sync completed", file=sys.stderr)
        print(f"[Event2AI MCP] {sync_result}", file=sys.stderr)
    except Exception as e:
        print(f"[Event2AI MCP] Initial sync failed: {e}", file=sys.stderr)

    # Start the server
    print("[Event2AI MCP] Server ready and waiting for requests", file=sys.stderr)
    async with stdio_server() as (read_stream, write_stream):
        await app.run(
            read_stream,
            write_stream,
            app.create_initialization_options()
        )


if __name__ == "__main__":
    asyncio.run(main())
