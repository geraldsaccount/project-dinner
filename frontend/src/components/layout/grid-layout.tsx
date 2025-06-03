import { buildGridCols, cn, type GridLayoutProps } from "@/lib/utils";
import type { FC } from "react";

const GridLayout: FC<GridLayoutProps> = ({
  children,
  className,
  gap = "gap-3",
  gridCols,
}) => {
  const gridClass = buildGridCols(gridCols);
  return (
    <div className={cn("grid", gridClass, gap, className)}>{children}</div>
  );
};

export default GridLayout;
