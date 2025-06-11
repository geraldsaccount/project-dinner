import PageHeader from "./page-header";

type Props = {
  title: string;
};

const LoadingHeader = ({ title }: Props) => {
  return (
    <div className="flex flex-col gap-4">
      <PageHeader title={title} className="animate-pulse" />
    </div>
  );
};

export default LoadingHeader;
