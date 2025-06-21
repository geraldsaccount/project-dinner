import * as React from "react";
import * as TabsPrimitive from "@radix-ui/react-tabs";

import { cn } from "@/lib/utils"; // Assuming cn is available or a simplified version

function Tabs({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Root>) {
  return (
    <TabsPrimitive.Root
      data-slot="tabs"
      className={cn("flex flex-col gap-2", className)}
      {...props}
    />
  );
}

function TabsList({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.List>) {
  return (
    <TabsPrimitive.List
      data-slot="tabs-list"
      // Brutalist changes for TabsList, using CSS variables:
      // - Removed rounded-lg for sharp corners
      // - Changed background to primary with primary-foreground text for stark contrast
      // - Added a strong border using the 'border' variable
      // - Removed padding to make it a more tightly packed block
      className={cn(
        "bg-primary text-primary-foreground inline-flex h-9 w-fit items-center justify-center border-2 border-border", // Brutalist styling with variables
        className
      )}
      {...props}
    />
  );
}

function TabsTrigger({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Trigger>) {
  return (
    <TabsPrimitive.Trigger
      data-slot="tabs-trigger"
      // Brutalist changes for TabsTrigger, using CSS variables:
      // - Removed rounded-md for sharp corners
      // - Removed shadow-sm for flat appearance
      // - Changed active state background to background with foreground text for high contrast
      // - Explicit border using 'border' variable for active state
      // - Removed soft transitions for abrupt changes
      // - Bold, uppercase text
      // - Strong focus outline using 'ring' variable
      className={cn(
        "data-[state=active]:bg-background data-[state=active]:text-foreground focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring text-primary-foreground inline-flex h-[calc(100%-1px)] flex-1 items-center justify-center gap-1.5 border-r-2 border-l-2 border-transparent px-3 py-1 text-sm font-bold uppercase whitespace-nowrap transition-none disabled:pointer-events-none disabled:opacity-50", // Brutalist styling with variables
        "data-[state=active]:border-border data-[state=active]:shadow-none", // Active state: solid border, no shadow
        "&:last-child]:border-r-0", // Remove right border from last child if it creates double border visually
        "&:first-child]:border-l-0", // Remove left border from first child if it creates double border visually
        className
      )}
      {...props}
    />
  );
}

function TabsContent({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Content>) {
  return (
    <TabsPrimitive.Content
      data-slot="tabs-content"
      // Brutalist changes for TabsContent, using CSS variables:
      // - Added a strong border to enclose the content using 'border' variable
      // - Increased padding for content area
      // - Changed background to background with foreground text for contrast
      className={cn(
        "flex-1 outline-none border-2 border-border p-6 bg-background text-foreground",
        className
      )} // Brutalist styling with variables
      {...props}
    />
  );
}

export { Tabs, TabsList, TabsTrigger, TabsContent };
