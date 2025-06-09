import { cn } from "@/lib/utils";

type Props = {
  title: string;
  className?: string;
};

const PageHeader = ({ title, className }: Props) => {
  return <h2 className={cn("text-5xl font-extrabold", className)}>{title}</h2>;
};

export default PageHeader;
