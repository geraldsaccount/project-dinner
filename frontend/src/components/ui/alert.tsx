import * as React from "react"
import { cva, type VariantProps } from "class-variance-authority"

import { cn } from "@/lib/utils"

const alertVariants = cva(
  // Brutalist changes for Alert base:
  // - Removed rounded-lg for sharp corners
  // - Replaced subtle border with strong 2px border
  // - Changed text-sm to text-base for more prominence
  // - Removed gap-y-0.5 for tighter spacing, relying on padding/margin
  // - Uses theme colors for background/text
  "relative w-full rounded-none border-2 px-4 py-3 text-base grid has-[>svg]:grid-cols-[calc(var(--spacing)*4)_1fr] grid-cols-[0_1fr] has-[>svg]:gap-x-3 items-start [&>svg]:size-4 [&>svg]:translate-y-0.5 [&>svg]:text-current",
  {
    variants: {
      variant: {
        default: "bg-background text-foreground border-primary", // Default: high contrast background/foreground, strong border
        destructive:
          "bg-destructive text-destructive-foreground border-destructive [&>svg]:text-current *:data-[slot=alert-description]:text-destructive-foreground", // Destructive: strong red, white text, red border
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
)

function Alert({
  className,
  variant,
  ...props
}: React.ComponentProps<"div"> & VariantProps<typeof alertVariants>) {
  return (
    <div
      data-slot="alert"
      role="alert"
      className={cn(alertVariants({ variant }), className)}
      {...props}
    />
  )
}

function AlertTitle({ className, ...props }: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="alert-title"
      // Brutalist AlertTitle:
      // - font-bold uppercase for strong typography
      // - Removed line-clamp for direct text display
      className={cn(
        "col-start-2 min-h-4 font-bold uppercase tracking-tight", // Brutalist styling
        className
      )}
      {...props}
    />
  )
}

function AlertDescription({
  className,
  ...props
}: React.ComponentProps<"div">) {
  return (
    <div
      data-slot="alert-description"
      // Brutalist AlertDescription:
      // - text-foreground for clarity (not muted)
      // - Removed gap-1 for tighter spacing
      className={cn(
        "col-start-2 grid justify-items-start text-sm [&_p]:leading-relaxed text-foreground", // Brutalist styling
        className
      )}
      {...props}
    />
  )
}

export { Alert, AlertTitle, AlertDescription }
