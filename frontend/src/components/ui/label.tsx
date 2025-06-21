// imported from shadcn/ui

"use client";

import * as React from "react";
import * as LabelPrimitive from "@radix-ui/react-label";

import { cn } from "@/lib/utils";

function Label({
  className,
  ...props
}: React.ComponentProps<typeof LabelPrimitive.Root>) {
  return (
    <LabelPrimitive.Root
      data-slot="label"
      className={cn(
        // Brutalist changes for Label:
        // - font-bold and uppercase for strong typography
        // - text-foreground for high contrast with background
        // - Removed gap-2 if not needed for typical label (can be added by parent)
        // - Ensured no rounded corners or subtle shadows (inherent in label usually)
        "flex items-center text-sm leading-none font-bold uppercase select-none text-foreground", // Brutalist styling
        "group-data-[disabled=true]:pointer-events-none group-data-[disabled=true]:opacity-50 peer-disabled:cursor-not-allowed peer-disabled:opacity-50", // Standard disabled states
        className
      )}
      {...props}
    />
  );
}

export { Label };
