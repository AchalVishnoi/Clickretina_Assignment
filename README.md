# Clickretina Assignment

## Working with AI

**Tools used:**
- Gemini — UI/layout building (Jetpack Compose screens)
- ChatGPT — debugging
- Android Studio Gemini (AI Assist) — autocomplete and logic suggestions while coding

**Sample prompts:**
1. "Use MaterialTheme typography for text sizing instead of manually assigning fontSize/fontWeight everywhere."
2. Pasted an existing `CourseCard` composable and asked: "Redesign the UI of this card according to the image shared. Take required data as arguments, and use default values for that data."
3. [add a real ChatGPT debugging prompt — you don't have one in here yet]

**What AI got right:**
On prompt 2, Gemini correctly restructured `CourseCard` into a horizontal layout matching the reference image — thumbnail box, level/title stacked in a column, rating and duration rows with icons — and kept the data (course, onClick) as parameters rather than hardcoding values.

**What AI got wrong (and the fix):**
I asked it to switch to `MaterialTheme.typography` for all text sizing, but it only applied that in `SectionHeader` (`MaterialTheme.typography.titleLarge`/`labelLarge`). Everywhere else — `CategoryCard`, `CourseCard`, `DashboardHeader`, `DashboardSearchBar` — it left manually hardcoded `fontSize`/`fontWeight` values (e.g. `fontSize = 16.sp, fontWeight = FontWeight.Bold`). It applied the instruction to the one component I happened to give as an example and didn't generalize it across the file. I went through each composable and swapped the manual sizes for the corresponding `MaterialTheme.typography` styles (titleMedium, bodyMedium, labelSmall, etc.) myself.
