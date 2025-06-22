import * as React from "react";
import * as TabsPrimitive from "@radix-ui/react-tabs";

function Tabs({
  className,
  ...props
}: React.ComponentProps<typeof TabsPrimitive.Root>) {
  return (
    <TabsPrimitive.Root
      data-slot="tabs"
      className={`block ${className}`} // Use 'block' for full width or adjust as needed
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
      // Brutalist tabs often have strong borders and no rounded corners
      className={`inline-flex h-auto items-center justify-center rounded-none bg-background p-0.5 text-foreground border-b-2 border-primary ${className}`}
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
      // Strong, clear, uppercase text with a prominent border and distinct active state
      className={`inline-flex items-center justify-center whitespace-nowrap border-2 border-transparent bg-transparent px-4 py-2 text-sm font-bold uppercase ring-offset-background hover:cursor-pointer focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 data-[state=active]:border-primary data-[state=active]:bg-primary data-[state=active]:text-primary-foreground data-[state=active]:shadow-none hover:bg-primary/90 hover:text-primary-foreground ${className}`}
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
      // Content area with a clear border, no rounded corners, and padding
      className={`mt-2 rounded-none border-2 border-primary p-6 ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 ${className}`}
      {...props}
    />
  );
}

export { Tabs, TabsList, TabsTrigger, TabsContent };
